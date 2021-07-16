package com.tokopedia.logisticaddaddress.features.district_recommendation

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.logisticCommon.data.entity.response.Data
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.databinding.BottomsheetDistcrictReccomendationRevampBinding
import com.tokopedia.logisticaddaddress.di.DaggerDistrictRecommendationComponent
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticaddaddress.features.addnewaddress.ChipsItemDecoration
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.ZipCodeChipsAdapter
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.analytics.AddNewAddressRevampAnalytics
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.DiscomAdapterRevamp
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.PopularCityAdapter
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoCleared
import rx.Emitter
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DiscomBottomSheetRevamp: BottomSheetUnify(),
        ZipCodeChipsAdapter.ActionListener,
        PopularCityAdapter.ActionListener, DiscomContract.View, DiscomAdapterRevamp.ActionListener{

    @Inject
    lateinit var presenter: DiscomContract.Presenter

    @Inject
    lateinit var userSession: UserSessionInterface

    private var viewBinding by autoCleared<BottomsheetDistcrictReccomendationRevampBinding>()

    private val zipCodeChipsAdapter by lazy { ZipCodeChipsAdapter(context, this) }
    private val popularCityAdapter by lazy { PopularCityAdapter(context, this) }
    private val listDistrictAdapter by lazy { DiscomAdapterRevamp(this) }
    private var discomRevampListener: DiscomRevampListener? = null
    private lateinit var chipsLayoutManagerZipCode: ChipsLayoutManager
    private var input: String = ""
    private var mIsInitialLoading: Boolean = false
    private var isPinpoint: Boolean = false
    private var isKodePosShown: Boolean = false
    private var postalCode: String = ""
    private var districtAddressData: Address? = null
    private var staticDimen8dp: Int? = 0
    private val handler = Handler()
    private val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    private val mEndlessListener = object : EndlessRecyclerViewScrollListener(mLayoutManager) {
        override fun onLoadMore(page: Int, totalItemsCount: Int) {
            presenter.loadData(input, page + 1)
        }
    }

    private val mCompositeSubs: CompositeSubscription = CompositeSubscription()

    private var fm: FragmentManager? = null

    interface DiscomRevampListener {
        fun onGetDistrict(districtAddress: Address)
        fun onChooseZipcode(districtAddress: Address, zipCode: String, isPinpoint: Boolean)
    }

    init {
        isDragable = true
        isHideable = true
        showCloseIcon = true
        isFullpage = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initLayout()
        initInjector()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewListener()
    }

    override fun onDetach() {
        super.onDetach()
        presenter.detach()
        mCompositeSubs.unsubscribe()
    }

    private fun initInjector() {
        DaggerDistrictRecommendationComponent.builder()
                .baseAppComponent((context?.applicationContext as BaseMainApplication).baseAppComponent)
                .build().inject(this)
        presenter.attach(this)
    }

    private fun initLayout() {
        viewBinding = BottomsheetDistcrictReccomendationRevampBinding.inflate(LayoutInflater.from(context), null, false)
        setupDiscomBottomsheet(viewBinding)
        setChild(viewBinding.root)
        setCloseClickListener {
            dismiss()
        }
        setOnDismissListener {
            dismiss()
        }
    }

    private fun setupDiscomBottomsheet(viewBinding: BottomsheetDistcrictReccomendationRevampBinding) {
        val cityList = resources.getStringArray(R.array.cityList)
        val chipsLayoutManager = ChipsLayoutManager.newBuilder(viewBinding.root.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()

        chipsLayoutManagerZipCode = ChipsLayoutManager.newBuilder(viewBinding.root.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()

        ViewCompat.setLayoutDirection(viewBinding.rvChips, ViewCompat.LAYOUT_DIRECTION_LTR)

        popularCityAdapter.cityList = cityList.toMutableList()

        viewBinding.run {
            rvListDistrict.visibility = View.GONE
            llZipCode.visibility = View.GONE
            bottomChoooseZipcode.visibility = View.GONE

            rvChips.apply {
                val dist = context.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
                layoutManager = chipsLayoutManager
                adapter = popularCityAdapter
                addItemDecoration(ChipsItemDecoration(dist))
            }

            rvListDistrict.apply {
                layoutManager = mLayoutManager
                adapter = listDistrictAdapter
            }
        }
    }

    private fun setViewListener() {
        viewBinding.searchPageInput.searchBarTextField.apply {
            setOnClickListener {
                AddNewAddressRevampAnalytics.onClickFieldCariKotaKecamatanNegative(userSession.userId)
            }
        }

        watchTextRx(viewBinding.searchPageInput.searchBarTextField)
                .subscribe { s ->
                    if (s.isNotEmpty()) {
                        input = s
                        mIsInitialLoading = true
                        handler.postDelayed({
                            presenter.loadData(input, 1)
                        }, 200)
                    } else {
                        viewBinding.tvDescInputDistrict.visibility = View.GONE
                        viewBinding.llPopularCity.visibility = View.VISIBLE
                        viewBinding.rvListDistrict.visibility = View.GONE
                    }
                }.toCompositeSubs()

        viewBinding.rvListDistrict.addOnScrollListener(mEndlessListener)

        viewBinding.btnChooseZipcode.setOnClickListener {
            if (viewBinding.etKodepos.textFieldInput.text.toString().length < 4) {
                AddNewAddressRevampAnalytics.onViewErrorToasterPilih(userSession.userId)
                AddNewAddressRevampAnalytics.onClickPilihKodePos(userSession.userId, NOT_SUCCESS)
                Toaster.build(it, "Kode pos terlalu pendek, min. 5 karakter.", Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
            } else {
                AddNewAddressRevampAnalytics.onClickPilihKodePos(userSession.userId, SUCCESS)
                districtAddressData?.let { data -> discomRevampListener?.onChooseZipcode(data, postalCode, isPinpoint) }
                dismiss()
            }
        }
    }

    private fun watchTextRx(view: EditText): Observable<String> {
        return Observable
                .create({ emitter: Emitter<String> ->
                    view.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(editable: Editable?) {
                            emitter.onNext(editable.toString())
                        }

                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    })
                }, Emitter.BackpressureMode.NONE)
                .filter { t -> t.isEmpty() || t.length > 2 }
                .debounce(700, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun Subscription.toCompositeSubs() {
        mCompositeSubs.add(this)
    }

    fun show(fm: FragmentManager?) {
        this.fm = fm
        fm?.let {
            show(it, "")
        }
    }

    fun setListener(listener: DiscomRevampListener) {
        this.discomRevampListener = listener
    }

    override fun onZipCodeClicked(zipCode: String) {
        TODO("Not yet implemented")
    }

    override fun onCityChipClicked(city: String) {
        TODO("Not yet implemented")
    }

    override fun renderData(list: List<Address>, hasNextPage: Boolean) {
        viewBinding.run {
            llPopularCity.visibility = View.GONE
            rvListDistrict.visibility = View.VISIBLE
            tvDescInputDistrict.visibility = View.VISIBLE
            tvDescInputDistrict.setText(R.string.hint_advice_search_address)

            if (mIsInitialLoading) {
                listDistrictAdapter.setData(list)
                mEndlessListener.resetState()
                mIsInitialLoading = false
            } else {
                listDistrictAdapter.appendData(list)
                mEndlessListener.updateStateAfterGetData()
            }
            mEndlessListener.setHasNextPage(hasNextPage)
        }
    }

    override fun showGetListError(throwable: Throwable) {
        val msg = ErrorHandler.getErrorMessage(context, throwable)
        Toaster.build(viewBinding.root, msg, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
    }

    override fun setLoadingState(active: Boolean) {
        viewBinding.run {
            searchPageInput.searchBarIcon.setOnClickListener {
                rvListDistrict.visibility = View.GONE
                llPopularCity.visibility = View.VISIBLE
                popularCityAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun showEmpty() {
        viewBinding.run {
            tvDescInputDistrict.visibility = View.VISIBLE
            tvDescInputDistrict.setText(R.string.hint_search_address_no_result)
            llPopularCity.visibility = View.VISIBLE
            rvListDistrict.visibility = View.GONE
        }
    }

    override fun setResultDistrict(data: Data, lat: Double, long: Double) {
       //no-op
    }

    override fun showToasterError() {
        //no-op
    }

    override fun onDistrictItemRevampClicked(districtModel: Address) {
        context?.let {
            districtModel.run {
                discomRevampListener?.onGetDistrict(districtModel)
                setupRvZipCodeChips()
                getDistrict(districtModel)
            }
        }
    }

    private fun setupRvZipCodeChips() {
        viewBinding.rvKodeposChips.apply {
            staticDimen8dp?.let { ChipsItemDecoration(it) }?.let { addItemDecoration(it) }
            layoutManager = chipsLayoutManagerZipCode
            adapter = zipCodeChipsAdapter
        }
    }

    fun getDistrict(data: Address) {
        districtAddressData = data
        viewBinding.run {
            llZipCode.visibility = View.VISIBLE
            bottomChoooseZipcode.visibility = View.VISIBLE
            searchPageInput.visibility = View.GONE
            tvDescInputDistrict.visibility = View.GONE
            rvListDistrict.visibility = View.GONE
            llPopularCity.visibility = View.GONE

            cardAddress.addressDistrict.text = "${data?.districtName}, ${data?.cityName}, ${data?.provinceName}"
             etKodepos.textFieldInput.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        AddNewAddressRevampAnalytics.onClickFieldKodePosNegative(userSession.userId)
                        openSoftKeyboard()
                        showZipCodes(data)
                    }
                }
                setOnClickListener {
                    AddNewAddressRevampAnalytics.onClickFieldKodePosNegative(userSession.userId)
                    openSoftKeyboard()
                    showZipCodes(data)
                }
            }
        }
    }

    private fun openSoftKeyboard() {
        viewBinding.etKodepos.textFieldInput.let {
            (it.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun showZipCodes(data: Address) {
        isKodePosShown = true
        ViewCompat.setLayoutDirection(viewBinding.rvKodeposChips, ViewCompat.LAYOUT_DIRECTION_LTR)
        data.zipCodes?.let {
            viewBinding.rvKodeposChips.visibility = View.VISIBLE
            zipCodeChipsAdapter.zipCodes = it.toMutableList()
            zipCodeChipsAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        private const val SUCCESS = "success"
        private const val NOT_SUCCESS = "not success"

    }
}