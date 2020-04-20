package com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewmodel

import com.tokopedia.topads.edit.data.param.NegKeyword
import com.tokopedia.topads.edit.data.response.GetKeywordResponse
import com.tokopedia.topads.edit.view.adapter.edit_keyword.EditKeywordListAdapterTypeFactory
import com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel.EditKeywordViewModel
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.EditNegKeywordListAdapterTypeFactory

/**
 * Created by Pika on 12/4/20.
 */

class EditNegKeywordItemViewModel(var data: NegKeyword) : EditNegKeywordViewModel(){
    override fun type(typesFactory: EditNegKeywordListAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }

}