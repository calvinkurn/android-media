package com.tokopedia.developer_options.remote_config

interface KeyValueListener {
    fun onListItemClick(selectedConfigKey: String)
    fun onEditorSaveButtonClick(editedConfigKey: String, editedConfigValue: String)
}
