package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.district_recommendation

import android.content.res.Resources
import android.os.Handler
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressModule
import com.tokopedia.logisticaddaddress.di.addnewaddress.DaggerAddNewAddressComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.ChipsItemDecoration
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_recommendation.DistrictRecommendationItemUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_recommendation.DistrictRecommendationResponseUiModel
import rx.Emitter
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-29.
 */
class DistrictRecommendationBottomSheetFragment : BottomSheets(),
        PopularCityRecommendationBottomSheetAdapter.ActionListener,
        DistrictRecommendationBottomSheetListener,
        DistrictRecommendationBottomSheetAdapter.ActionListener {

    private var bottomSheetView: View? = null
    private lateinit var popularCityAdapter: PopularCityRecommendationBottomSheetAdapter
    private lateinit var listDistrictAdapter: DistrictRecommendationBottomSheetAdapter
    private lateinit var rvChips: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var llListDistrict: LinearLayout
    private lateinit var llPopularCity: LinearLayout
    private lateinit var rvListDistrict: RecyclerView
    private lateinit var icCloseBtn: ImageView
    private var isLoading: Boolean = false
    private var input: String = ""
    private val mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    private val mEndlessListener = object : EndlessRecyclerViewScrollListener(mLayoutManager) {
        override fun onLoadMore(page: Int, totalItemsCount: Int) {
            presenter.getDistrictRecommendation(input, page + 1)
        }
    }
    private var mIsInitialLoading: Boolean = false
    private val mCompositeSubs: CompositeSubscription = CompositeSubscription()
    val handler = Handler()
    private lateinit var actionListener: ActionListener

    @Inject
    lateinit var presenter: DistrictRecommendationBottomSheetPresenter

    override fun getLayoutResourceId(): Int {
        return R.layout.bottomsheet_district_recommendation
    }

    override fun initView(view: View) {
        prepareLayout(view)
        initInjector()
        setViewListener()
    }

    private fun prepareLayout(view: View) {
        bottomSheetView = view
        rvChips = view.findViewById(R.id.rv_chips)
        etSearch = view.findViewById(R.id.et_search_district_recommendation)
        llPopularCity = view.findViewById(R.id.ll_popular_city)
        rvListDistrict = view.findViewById(R.id.rv_list_district)
        icCloseBtn = view.findViewById(R.id.ic_close)
        llListDistrict = view.findViewById(R.id.ll_list_district)
        llListDistrict.visibility = View.GONE

        val cityList = resources.getStringArray(R.array.cityList)
        val chipsLayoutManager = ChipsLayoutManager.newBuilder(view.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()

        ViewCompat.setLayoutDirection(rvChips, ViewCompat.LAYOUT_DIRECTION_LTR)
        popularCityAdapter = PopularCityRecommendationBottomSheetAdapter(context, this)
        popularCityAdapter.cityList = cityList.toMutableList()

        rvChips.apply {
            val dist = context.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_8)
            layoutManager = chipsLayoutManager
            adapter = popularCityAdapter
            addItemDecoration(ChipsItemDecoration(dist))
        }

        listDistrictAdapter = DistrictRecommendationBottomSheetAdapter(this)
        rvListDistrict.apply {
            layoutManager = mLayoutManager
            adapter = listDistrictAdapter
        }

        etSearch.setSelection(etSearch.text.length)
    }

    override fun onDetach() {
        super.onDetach()
        presenter.detachView()
        mCompositeSubs.unsubscribe()
    }

    override fun title(): String {
        return getString(R.string.kota_kecamatan)
    }

    override fun state(): BottomSheetsState {
        return BottomSheetsState.FULL
    }

    override fun configView(parentView: View?) {
        super.configView(parentView)
        parentView?.findViewById<View>(R.id.layout_title)?.setOnClickListener(null)
        parentView?.findViewById<View>(R.id.btn_close)?.setOnClickListener {
            AddNewAddressAnalytics.eventClickBackArrowOnNegativePage()
            onCloseButtonClick()
        }
    }

    override fun showData(model: DistrictRecommendationResponseUiModel, hasNext: Boolean) {
        if (model.listDistrict.isNotEmpty()) {
            isLoading = false
            llPopularCity.visibility = View.GONE
            llListDistrict.visibility = View.VISIBLE
            if (mIsInitialLoading) {
                listDistrictAdapter.setData(model.listDistrict.toMutableList())
                mEndlessListener.resetState()
                mIsInitialLoading = false
            } else {
                listDistrictAdapter.appendData(model.listDistrict.toMutableList())
                mEndlessListener.updateStateAfterGetData()
            }
            mEndlessListener.setHasNextPage(hasNext)
        }
    }

    override fun onCityChipClicked(city: String) {
        etSearch.setText(city)
        etSearch.setSelection(city.length)
        AddNewAddressAnalytics.eventClickChipsKotaKecamatanChangeAddressNegative()
    }

    fun setActionListener(actionListener: ActionListener) {
        this.actionListener = actionListener
    }

    private fun initInjector() {
        activity?.run {
            DaggerAddNewAddressComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .addNewAddressModule(AddNewAddressModule())
                    .build()
                    .inject(this@DistrictRecommendationBottomSheetFragment)
            presenter.attachView(this@DistrictRecommendationBottomSheetFragment)
        }
    }

    private fun setViewListener() {
        etSearch.apply {
            isFocusableInTouchMode = true
            requestFocus()
        }
        watchTextRx(etSearch)
                .subscribe { s ->
                    if (s.isNotEmpty()) {
                        input = s
                        showClearBtn()
                        mIsInitialLoading = true
                        handler.postDelayed({
                            presenter.clearCacheDistrictRecommendation()
                            presenter.getDistrictRecommendation(input, 1)
                        }, 200)
                    } else icCloseBtn.visibility = View.GONE
                }.toCompositeSubs()

        rvListDistrict.addOnScrollListener(mEndlessListener)
    }

    private fun showClearBtn() {
        icCloseBtn.visibility = View.VISIBLE
        icCloseBtn.setOnClickListener {
            etSearch.setText("")
            llListDistrict.visibility = View.GONE
            llPopularCity.visibility = View.VISIBLE
            popularCityAdapter.notifyDataSetChanged()
            icCloseBtn.visibility = View.GONE
        }
    }

    override fun onDistrictItemClicked(districtRecommendationItemUiModel: DistrictRecommendationItemUiModel) {
        context?.let {
            districtRecommendationItemUiModel.run {
                actionListener.onGetDistrict(districtRecommendationItemUiModel)
                AddNewAddressAnalytics.eventClickSuggestionKotaKecamatanChangeAddressNegative()
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

    interface ActionListener {
        fun onGetDistrict(districtRecommendationItemUiModel: DistrictRecommendationItemUiModel)
    }

    companion object {
        @JvmStatic
        fun newInstance(): DistrictRecommendationBottomSheetFragment {
            return DistrictRecommendationBottomSheetFragment()
        }
    }
}
