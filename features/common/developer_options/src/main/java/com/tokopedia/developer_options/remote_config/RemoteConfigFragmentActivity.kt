package com.tokopedia.developer_options.remote_config

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import com.tokopedia.developer_options.R

import com.tokopedia.developer_options.remote_config.adapters.RemoteConfigListAdapter
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig

class RemoteConfigFragmentActivity : FragmentActivity(), RemoteConfigListener {

    companion object {
        const val ARGS_SELECTED_KEY = "selected_key"
    }

    private lateinit var listAdapter: RemoteConfigListAdapter
    private var remoteConfig: RemoteConfig? = null
    private var isListEmpty: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remote_config)

        initView()
    }

    override fun onListItemClick(selectedConfigKey: String) {
        val fragmentBundle = Bundle()
        fragmentBundle.putString(ARGS_SELECTED_KEY, selectedConfigKey)

        val dialog = RemoteConfigEditorDialog()
        dialog.arguments = fragmentBundle
        dialog.showDialog(supportFragmentManager, this)
    }

    override fun onEditorSaveButtonClick(editedConfigKey: String, editedConfigValue: String) {
        if (!TextUtils.isEmpty(editedConfigKey)) {
            remoteConfig?.setString(editedConfigKey, editedConfigValue)

            updateListAdapterData()
        }
    }

    private fun getPrefixes(vararg prefixes: String): Set<String> {
        val keysSet: MutableSet<String> = mutableSetOf()

        remoteConfig?.let {
            for (prefix in prefixes) {
                keysSet.apply { addAll(remoteConfig!!.getKeysByPrefix(prefix) ?: setOf()) }
            }
        }

        if (keysSet.isEmpty()) {
            isListEmpty = true
        }

        return keysSet
    }

    private fun updateListAdapterData() {
        if (remoteConfig != null) {
            val prefix = intent?.extras?.getString(DeveloperOptionActivity.REMOTE_CONFIG_PREFIX) ?: ""

            val configListData = when {
                prefix.isNotEmpty() -> getPrefixes(prefix)
                else -> getPrefixes("mainapp", "android", "app")
            }

            listAdapter.setConfigData(remoteConfig, configListData)
        }
    }

    private fun initView() {
        remoteConfig = FirebaseRemoteConfigImpl(this)
        listAdapter = RemoteConfigListAdapter(this)

        updateListAdapterData()

        val rvConfigList: RecyclerView = findViewById(R.id.config_list_container)

        if (!isListEmpty) {
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val dividerItemDecoration = DividerItemDecoration(rvConfigList.context, layoutManager.orientation)
            rvConfigList.layoutManager = layoutManager
            rvConfigList.addItemDecoration(dividerItemDecoration)
            rvConfigList.adapter = listAdapter
        } else {
            rvConfigList.visibility = View.GONE
            findViewById<AppCompatTextView>(R.id.empty_message).visibility = View.VISIBLE
        }
    }
}
