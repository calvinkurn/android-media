package com.tokopedia.developer_options.remote_config

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import com.tokopedia.developer_options.R

import com.tokopedia.developer_options.remote_config.adapters.KeyValueListAdapter
import com.tokopedia.developer_options.presentation.activity.NewDeveloperOptionActivity
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unifycomponents.SearchBarUnify

class RemoteConfigFragmentActivity : FragmentActivity(), KeyValueListener {

    companion object {
        const val ARGS_SELECTED_KEY = "selected_key"
        const val ARGS_SELECTED_VALUE = "selected_value"
    }

    private lateinit var listAdapter: KeyValueListAdapter
    private var remoteConfig: RemoteConfig? = null
    private var isListEmpty: Boolean = false
    private var rvConfigList: RecyclerView? = null
    private var searchBarRemoteConfig: SearchBarUnify? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remote_config)

        initView()
        initSearchbarView()
    }

    override fun onListItemClick(selectedConfigKey: String) {
        showEditRemoteConfigDialog(selectedConfigKey)
    }

    fun showEditRemoteConfigDialog(keyToEdit: String) {
        val fragmentBundle = Bundle()
        fragmentBundle.putString(ARGS_SELECTED_KEY, keyToEdit)
        fragmentBundle.putString(ARGS_SELECTED_VALUE, remoteConfig?.getString(keyToEdit) ?: "")

        val dialog = KeyValueEditorDialog()
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
                keysSet.apply { addAll(remoteConfig?.getKeysByPrefix(prefix) ?: setOf()) }
            }
        }

        if (keysSet.isEmpty()) {
            isListEmpty = true
        }

        return keysSet
    }

    private fun updateListAdapterData() {
        if (remoteConfig != null) {
            val prefix =
                intent?.extras?.getString(NewDeveloperOptionActivity.REMOTE_CONFIG_PREFIX) ?: ""

            val configListData = when {
                prefix.isNotEmpty() -> getPrefixes(prefix)
                else -> getPrefixes("mainapp", "android", "app")
            }

            updateVisibility(configListData.isNotEmpty())
            listAdapter.setConfigData(remoteConfig, configListData)
        }
    }

    private fun initView() {
        remoteConfig = FirebaseRemoteConfigImpl(this)
        listAdapter = KeyValueListAdapter(this)

        updateListAdapterData()

        rvConfigList = findViewById(R.id.config_list_container)
        searchBarRemoteConfig = findViewById(R.id.searchBarRemoteConfig)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        rvConfigList?.layoutManager = layoutManager
        rvConfigList?.addItemDecoration(dividerItemDecoration)
        rvConfigList?.adapter = listAdapter
        findViewById<View>(R.id.button_add_empty)?.setOnClickListener {
            showEditRemoteConfigDialog(
                intent?.extras?.getString(NewDeveloperOptionActivity.REMOTE_CONFIG_PREFIX) ?: ""
            )
        }
        updateVisibility(!isListEmpty)
    }

    private fun initSearchbarView() {
        searchBarRemoteConfig?.searchBarTextField?.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = searchBarRemoteConfig?.searchBarTextField?.text.toString()
                    listAdapter.filter.filter(query)
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    listAdapter.filter.filter(s.toString())
                }
            })
        }
    }

    private fun updateVisibility(mainDataVisible: Boolean) {
        if (mainDataVisible) {
            rvConfigList?.visibility = View.VISIBLE
            findViewById<View>(R.id.empty_group).visibility = View.GONE
        } else {
            rvConfigList?.visibility = View.GONE
            findViewById<View>(R.id.empty_group).visibility = View.VISIBLE
        }
    }
}
