package com.tokopedia.topads.view.adapter.keyword.viewmodel

import com.tokopedia.topads.view.adapter.keyword.KeywordListAdapterTypeFactory

/**
 * Author errysuprayogi on 12,November,2019
 */
abstract class KeywordViewModel {
    abstract fun type(typesFactory: KeywordListAdapterTypeFactory): Int

//    override fun equals(other: Any?): Boolean {
//        if (other is KeywordItemViewModel) {
//            return (this is KeywordItemViewModel).data.keyword.equals(other.data.keyword)
//        }
//        return false
//    }
//
//    override fun hashCode(): Int {
//        return 3*(this as KeywordItemViewModel).data.totalSearch
//    }
}



