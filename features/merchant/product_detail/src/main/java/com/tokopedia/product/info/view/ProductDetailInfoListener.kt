package com.tokopedia.product.info.view

import com.tokopedia.product.detail.data.model.datamodel.ProductDetailInfoContent

/**
 * Created by Yehezkiel on 12/10/20
 */
interface ProductDetailInfoListener {
    fun closeAllExpand(uniqueIdentifier: Int, toggle: Boolean)
    fun onBranchLinkClicked(url: String)
    fun goToVideoPlayer(url: List<String>, index: Int)
    fun goToShopNotes(title: String, date: String, desc: String)
    fun goToSpecification(annotation: List<ProductDetailInfoContent>)
    fun goToImagePreview(url: String)
    fun goToApplink(url: String)
    fun goToDiscussion(discussionCount: Int)
}