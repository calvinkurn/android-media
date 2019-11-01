package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.view.sheet.InfoSheetProductList
import com.tokopedia.topads.view.sheet.ProductFilterSheetList
import com.tokopedia.topads.view.sheet.ProductSortSheetList
import kotlinx.android.synthetic.main.topads_create_fragment_product_list.*
import kotlinx.android.synthetic.main.topads_create_fragment_product_list.tip_btn

/**
 * Author errysuprayogi on 29,October,2019
 */
class CreateManualAdsProductAdsListFragment: CreateManualAdsBaseStepperFragment<CreateManualAdsStepperModel>() {

    private lateinit var sortProductList: ProductSortSheetList
    private lateinit var filteSheetProductList: ProductFilterSheetList

    companion object {
        fun createInstance(): Fragment {

            val fragment = CreateManualAdsProductAdsListFragment()
            val args = Bundle()
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun initiateStepperModel() {
        stepperModel = stepperModel?: CreateManualAdsStepperModel()
    }

    override fun saveStepperModel(stepperModel: CreateManualAdsStepperModel) {}

    override fun gotoNextPage() {
        stepperListener?.goToNextPage(stepperModel)
    }

    override fun populateView(stepperModel: CreateManualAdsStepperModel) {
    }

    override fun getScreenName(): String {
        return CreateManualAdsCreateGroupAdsFragment::class.java.simpleName
    }

    override fun initInjector() {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_create_fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            sortProductList = ProductSortSheetList.newInstance(it)
            filteSheetProductList = ProductFilterSheetList.newInstance(it)
        }
        btn_next.setOnClickListener {
            gotoNextPage()
        }
        tip_btn.setOnClickListener {
            InfoSheetProductList.newInstance(it.context).show()
        }
        btn_sort.setOnClickListener {
            sortProductList.show()
        }
        btn_filter.setOnClickListener {
            filteSheetProductList.show()
        }
        sortProductList.setActionListener(object: ProductSortSheetList.ActionListener{
            override fun onSort(pos: Int) {
                
            }
        })
    }
}