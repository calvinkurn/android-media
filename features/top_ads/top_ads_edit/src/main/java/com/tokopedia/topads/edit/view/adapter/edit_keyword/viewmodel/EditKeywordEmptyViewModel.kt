package com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel

import com.tokopedia.topads.edit.view.adapter.edit_keyword.EditKeywordListAdapterTypeFactory

/**
 * Created by Pika on 9/4/20.
 */

class EditKeywordEmptyViewModel:EditKeywordViewModel() {
    override fun type(typesFactory: EditKeywordListAdapterTypeFactory): Int {
        return typesFactory.type(this)

    }

}