package com.tokopedia.play.broadcaster.data.config

import javax.inject.Inject

/**
 * Created by jegul on 03/07/20
 */
class ProductConfigStoreImpl @Inject constructor(

): ProductConfigStore {

    /**
     * Default Value:
     * Min Product = 1
     * Max Product = 15
     * [Docs](https://docs.google.com/document/d/1qRGR3mS_6bINejj6otfWF7zuJ9rgcRlLMQ1BeE5FmEw/edit#)
     */
    private var mMinProduct: Int = 1
    private var mMaxProduct: Int = 15
    private var mMaxProductDesc: String = ""

    override fun setMaxProduct(count: Int) {
        mMaxProduct = count
    }

    override fun setMinProduct(count: Int) {
        mMinProduct = count
    }

    override fun setMaxProductDesc(desc: String) {
        mMaxProductDesc = desc
    }

    override fun getMaxProduct(): Int {
        return mMaxProduct
    }

    override fun getMinProduct(): Int {
        return mMinProduct
    }

    override fun getMaxProductDesc(): String {
        return mMaxProductDesc
    }
}