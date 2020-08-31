package com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewmodel

import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.EditNegKeywordListAdapterTypeFactory


/**
 * Created by Pika on 12/4/20.
 */

class EditNegKeywordEmptyViewModel : EditNegKeywordViewModel() {
    override fun type(typesFactory: EditNegKeywordListAdapterTypeFactory): Int {
        return typesFactory.type(this)

    }

}