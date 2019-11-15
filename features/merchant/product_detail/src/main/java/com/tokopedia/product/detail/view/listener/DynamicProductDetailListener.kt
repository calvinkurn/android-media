package com.tokopedia.product.detail.view.listener

import android.view.View
import com.tokopedia.product.detail.common.data.model.product.Category
import com.tokopedia.product.detail.common.data.model.product.Video
import com.tokopedia.product.detail.data.model.description.DescriptionData
import com.tokopedia.product.detail.data.model.spesification.Specification

interface DynamicProductDetailListener {
    val onViewClickListener: View.OnClickListener

    /**
     * ProductInfoViewHolder
     */
    fun openCategory(category: Category.Detail)
    fun gotoEtalase(etalaseId: String, shopID: Int)
    fun gotoVideoPlayer(videos: List<Video>, index: Int)
    fun gotoDescriptionTab(data: DescriptionData, listOfCatalog: ArrayList<Specification>)

    /**
     * ProductDiscussionViewHolder
     */
    fun onDiscussionClicked()
    fun removeDiscussionSection()
}