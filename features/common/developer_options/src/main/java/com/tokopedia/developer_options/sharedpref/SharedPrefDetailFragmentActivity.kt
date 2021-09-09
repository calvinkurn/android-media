package com.tokopedia.developer_options.sharedpref

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.SHARED_PREF_FILE
import com.tokopedia.developer_options.remote_config.KeyValueEditorDialog
import com.tokopedia.developer_options.remote_config.KeyValueListener
import com.tokopedia.developer_options.remote_config.adapters.KeyValueListAdapter

class SharedPrefDetailFragmentActivity : FragmentActivity(), KeyValueListener {

    companion object {
        const val ARGS_SELECTED_KEY = "selected_key"
        const val ARGS_SELECTED_VALUE = "selected_value"
    }

    private lateinit var listAdapter: KeyValueListAdapter
    private var isListEmpty: Boolean = false
    private lateinit var sharedPref: SharedPreferences
    private var rvConfigList:RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remote_config)

        initView()
    }

    override fun onListItemClick(selectedConfigKey: String) {
        showEditDialog(selectedConfigKey)
    }

    fun showEditDialog(keyToEdit:String) {
        val fragmentBundle = Bundle()
        fragmentBundle.putString(ARGS_SELECTED_KEY, keyToEdit)
        fragmentBundle.putString(ARGS_SELECTED_VALUE, sharedPref.getString(keyToEdit, ""))

        val dialog = KeyValueEditorDialog()
        dialog.arguments = fragmentBundle
        dialog.showDialog(supportFragmentManager, this)
    }

    override fun onEditorSaveButtonClick(editedConfigKey: String, editedConfigValue: String) {
        if (!TextUtils.isEmpty(editedConfigKey)) {
            sharedPref.edit().putString(editedConfigKey, editedConfigValue).apply()
            updateListAdapterData()
        }
    }

    private fun updateListAdapterData() {
        val keyList = sharedPref.all.map { it ->
            Pair(it.key, it.value.toString())
        }.toList()
        updateVisibility(keyList.isNotEmpty())
        listAdapter.setData(keyList)
    }

    private fun initView() {
        sharedPref = this.getSharedPreferences(intent.extras?.getString(SHARED_PREF_FILE), Context.MODE_PRIVATE)
        listAdapter = KeyValueListAdapter(this)

        updateListAdapterData()

        rvConfigList = findViewById(R.id.config_list_container)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        rvConfigList?.layoutManager = layoutManager
        rvConfigList?.addItemDecoration(dividerItemDecoration)
        rvConfigList?.adapter = listAdapter
        findViewById<View>(R.id.button_add_empty)?.setOnClickListener {
            showEditDialog("")
        }
        updateVisibility(!isListEmpty)
    }

    private fun updateVisibility(mainDataVisible:Boolean) {
        if (mainDataVisible) {
            rvConfigList?.visibility = View.VISIBLE
            findViewById<View>(R.id.empty_group).visibility = View.GONE
        } else {
            rvConfigList?.visibility = View.GONE
            findViewById<View>(R.id.empty_group).visibility = View.VISIBLE
        }
    }
}
