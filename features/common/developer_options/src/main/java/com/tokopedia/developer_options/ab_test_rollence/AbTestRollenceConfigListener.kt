package com.tokopedia.developer_options.ab_test_rollence

interface AbTestRollenceConfigListener {
    fun onListItemClick(selectedConfigKey: String)
    fun onEditorSaveButtonClick(editedConfigKey: String, editedConfigValue: String)
}


