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
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressModule
import com.tokopedia.logisticaddaddress.di.addnewaddress.DaggerAddNewAddressComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.ChipsItemDecoration
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.AddEditAddressFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_recommendation.DistrictRecommendationItemUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_recommendation.DistrictRecommendationResponseUiModel
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
    private var numPage: Int = 1
    private val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    val handler = Handler()
    private lateinit var actionListener: ActionListener

    @Inject
    lateinit var presenter: DistrictRecommendationBottomSheetPresenter

    interface ActionListener {
        fun onGetDistrict(districtRecommendationItemUiModel: DistrictRecommendationItemUiModel)
    }

    companion object {
        @JvmStatic
        fun newInstance(): DistrictRecommendationBottomSheetFragment {
            return DistrictRecommendationBottomSheetFragment()
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottomsheet_district_recommendation
    }

    override fun initView(view: View) {
        prepareLayout(view)
        if (activity != null) {
            initInjector()
        }

        val staticDimen8dp = view.context?.resources?.getDimensionPixelOffset(R.dimen.dp_8)
        setViewListener(staticDimen8dp)
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

        val res: Resources = resources
        val cityList = res.getStringArray(R.array.cityList)
        val chipsLayoutManager = ChipsLayoutManager.newBuilder(view.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()

        ViewCompat.setLayoutDirection(rvChips, ViewCompat.LAYOUT_DIRECTION_LTR)
        popularCityAdapter = PopularCityRecommendationBottomSheetAdapter(context, this)
        popularCityAdapter.cityList = cityList.toMutableList()

        rvChips.apply {
            layoutManager = chipsLayoutManager
            adapter = popularCityAdapter
        }

        listDistrictAdapter = DistrictRecommendationBottomSheetAdapter(this)
        rvListDistrict.apply {
            layoutManager = linearLayoutManager
            adapter = listDistrictAdapter
        }

        etSearch.setSelection(etSearch.text.length)
    }

    fun setActionListener(actionListener: AddEditAddressFragment) {
        this.actionListener = actionListener
    }

    fun initInjector() {
        activity?.run {
            DaggerAddNewAddressComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .addNewAddressModule(AddNewAddressModule())
                    .build()
                    .inject(this@DistrictRecommendationBottomSheetFragment)
            presenter.attachView(this@DistrictRecommendationBottomSheetFragment)
        }
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

    override fun onCityChipClicked(city: String) {
        etSearch.setText(city)
        AddNewAddressAnalytics.eventClickChipsKotaKecamatanChangeAddressNegative()
    }

    private fun setViewListener(staticDimen8dp: Int?) {
        etSearch.apply {
            isFocusableInTouchMode = true
            requestFocus()
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                               after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                           count: Int) {
                    if (s.isNotEmpty()) {
                        input = "$s"
                        showClearBtn()
                        handler.postDelayed({
                            presenter.clearCacheDistrictRecommendation()
                            presenter.getDistrictRecommendation(input, "1")
                        }, 500)

                    } else {
                        icCloseBtn.visibility = View.GONE
                    }
                }

                override fun afterTextChanged(s: Editable) {
                }
            })
        }

        staticDimen8dp?.let { ChipsItemDecoration(staticDimen8dp) }?.let { rvChips.addItemDecoration(it) }

        var visibleItemCount: Int
        var totalItemCount: Int
        var pastVisibleItem: Int
        rvListDistrict.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = linearLayoutManager.childCount
                totalItemCount = linearLayoutManager.itemCount
                pastVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()
                if ((visibleItemCount + pastVisibleItem) >= totalItemCount && !isLoading) {
                    isLoading = true
                    numPage += 1
                    presenter.clearCacheDistrictRecommendation()
                    presenter.getDistrictRecommendation(input, numPage.toString())
                }
            }
        })
    }

    private fun showClearBtn() {
        icCloseBtn.setOnClickListener {
            etSearch.setText("")
            llListDistrict.visibility = View.GONE
            llPopularCity.visibility = View.VISIBLE
            popularCityAdapter.notifyDataSetChanged()
            icCloseBtn.visibility = View.GONE
        }
    }

    override fun onSuccessGetDistrictRecommendation(getDistrictRecommendationResponseUiModel: DistrictRecommendationResponseUiModel, numPage: String) {
        if (getDistrictRecommendationResponseUiModel.listDistrict.isNotEmpty()) {
            isLoading = false
            llPopularCity.visibility = View.GONE
            llListDistrict.visibility = View.VISIBLE
            if (numPage == "1") {
                listDistrictAdapter.loadDistrictRecommendation(getDistrictRecommendationResponseUiModel.listDistrict.toMutableList())
            } else {
                listDistrictAdapter.loadDistrictRecommendationNextPage(getDistrictRecommendationResponseUiModel.listDistrict.toMutableList())
            }
            listDistrictAdapter.notifyDataSetChanged()
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
}