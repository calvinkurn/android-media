package com.tokopedia.shop.review.product.view.adapter

/**
 * Created by zulfikarrahman on 1/16/18.
 */
class ReviewProductModelTitleHeader(val title: String) : ReviewProductModel {
    override fun type(typeFactory: ReviewProductTypeFactoryAdapter?): Int {
        return typeFactory?.type(this) ?: -1
    }

}