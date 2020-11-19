package com.tokopedia.developer_options.remote_config

interface RemoteConfigListener {
    fun onListItemClick(selectedConfigKey: String, editedConfigValue: String)
    fun onEditorSaveButtonClick(editedConfigKey: String, editedConfigValue: String)
}
