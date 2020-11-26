package com.tokopedia.deals.common.listener

import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView

/**
 * @author by jessica on 17/06/20
 */

interface DealsBrandActionListener {
    fun onClickBrand(brand: DealsBrandsDataView.Brand, position: Int)
    fun onClickSeeAllBrand(seeAllUrl: String)
    fun onImpressionBrand(brand: DealsBrandsDataView.Brand, position: Int)
    fun showTitle(brand:DealsBrandsDataView)
}