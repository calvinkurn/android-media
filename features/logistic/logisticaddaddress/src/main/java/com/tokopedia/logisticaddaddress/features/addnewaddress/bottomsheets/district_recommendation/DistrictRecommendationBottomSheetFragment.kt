package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.district_recommendation

import android.content.res.Resources
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.logisticaddaddress.R
import kotlinx.android.synthetic.main.bottomsheet_district_recommendation.*

/**
 * Created by fwidjaja on 2019-05-29.
 */
class DistrictRecommendationBottomSheetFragment: BottomSheets(), PopularCityRecommendationBottomSheetAdapter.ActionListener {
    private lateinit var adapter: PopularCityRecommendationBottomSheetAdapter
    private lateinit var rvChips: RecyclerView
    private lateinit var etSearch: EditText

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
        rvChips = view.findViewById(R.id.rv_chips)
        etSearch = view.findViewById(R.id.et_search)
        val res: Resources = resources
        val cityList = res.getStringArray(R.array.cityList)
        val layoutManager = ChipsLayoutManager.newBuilder(view.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
        val staticDimen8dp = view.context?.resources?.getDimensionPixelOffset(R.dimen.dp_8)
        rvChips.addItemDecoration(staticDimen8dp?.let { ChipsItemDecoration(staticDimen8dp) })
        rvChips.layoutManager = layoutManager
        ViewCompat.setLayoutDirection(rvChips, ViewCompat.LAYOUT_DIRECTION_LTR)
        adapter = PopularCityRecommendationBottomSheetAdapter(context, this)
        adapter.setCityList(cityList)
        rvChips.adapter = adapter
        updateHeight()
    }

    override fun title(): String {
        return getString(R.string.kota_kecamatan)
    }

    override fun configView(parentView: View?) {
        super.configView(parentView)
        parentView?.findViewById<View>(R.id.layout_title)?.setOnClickListener(null)
        parentView?.findViewById<View>(R.id.btn_close)?.setOnClickListener{ onCloseButtonClick() }
    }

    override fun onCityChipClicked(city: String) {
        etSearch.setText(city)
    }
}