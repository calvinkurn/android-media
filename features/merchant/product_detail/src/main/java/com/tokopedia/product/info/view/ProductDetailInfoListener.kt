package com.tokopedia.product.info.view

import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoContent
import com.tokopedia.product.info.data.response.ShopNotesData

/**
 * Created by Yehezkiel on 12/10/20
 */
interface ProductDetailInfoListener {
    fun closeAllExpand(uniqueIdentifier: Int, toggle: Boolean)
    fun onBranchLinkClicked(url: String)
    fun goToVideoPlayer(url: List<String>, index: Int)
    fun goToShopNotes(shopNotesData: ShopNotesData)
    fun goToSpecification(annotation: List<ProductDetailInfoContent>)
    fun goToImagePreview(url: String)
    fun goToApplink(url: String)
    fun goToCategory(url: String)
    fun goToEtalase(url: String)
    fun goToCatalog(url: String, catalogName: String)
    fun goToDiscussion(discussionCount: Int)
    fun onCustomInfoClicked(url: String)
    fun goToEducational(url: String, infoTitle: String, infoValue: String, position: Int)
    fun onImpressInfo(infoTitle: String, infoValue: String, position: Int)
    fun onImpressCatalog(key: String, value: String, position: Int)
}