package com.tokopedia.developer_options.ab_test_rollence

interface AbTestRollenceConfigListener {
    fun onListItemClick(selectedConfigKey: String, selectedConfigKeyValue: String)
    fun onEditorSaveButtonClick(editedConfigKey: String, editedConfigValue: String)
    fun onEditorDeleteKeyButtonClick(editedConfigKey: String)
}


