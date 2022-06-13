package com.tokopedia.developer_options.sharedpref

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity
import com.tokopedia.developer_options.remote_config.KeyValueEditorDialog
import com.tokopedia.developer_options.remote_config.KeyValueListener
import com.tokopedia.developer_options.remote_config.adapters.KeyValueListAdapter

class SharedPrefDetailFragmentActivity : FragmentActivity(), KeyValueListener {

    companion object {
        const val ARGS_SELECTED_KEY = "selected_key"
        const val ARGS_SELECTED_VALUE = "selected_value"
        const val TYPE_LONG = 0
        const val TYPE_INT = 1
        const val TYPE_FLOAT = 2
        const val TYPE_BOOLEAN = 3
        const val TYPE_STRING_SET = 4
        const val TYPE_STRING = 5
        const val TYPE_UNKNOWN = -1
    }

    var sharedPrefValueType = TYPE_UNKNOWN

    private lateinit var listAdapter: KeyValueListAdapter
    private var isListEmpty: Boolean = false
    private lateinit var sharedPref: SharedPreferences
    private var rvConfigList: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remote_config)

        initView()
    }

    override fun onListItemClick(selectedConfigKey: String) {
        showEditDialog(selectedConfigKey)
    }

    fun showEditDialog(keyToEdit: String) {
        val fragmentBundle = Bundle()
        fragmentBundle.putString(ARGS_SELECTED_KEY, keyToEdit)
        var value = ""
        var success = false
        var i = 0
        while (!success) {
            try {
                value = when (i) {
                    TYPE_LONG -> sharedPref.getLong(keyToEdit, 0L).toString()
                    TYPE_INT -> sharedPref.getInt(keyToEdit, 0).toString()
                    TYPE_FLOAT -> sharedPref.getFloat(keyToEdit, 0F).toString()
                    TYPE_BOOLEAN -> sharedPref.getBoolean(keyToEdit, false).toString()
                    TYPE_STRING_SET -> sharedPref.getStringSet(keyToEdit, setOf())
                        ?.joinToString("#") ?: ""
                    TYPE_STRING -> sharedPref.getString(keyToEdit, "").toString()
                    else -> {
                        Toast.makeText(this, "No Data Type is not recognized", Toast.LENGTH_SHORT).show()
                        ""
                    }
                }
                sharedPrefValueType = i
                success = true
            } catch (e: Exception) {
                success = false
            }
            i++
        }
        fragmentBundle.putString(ARGS_SELECTED_VALUE, value)

        val dialog = KeyValueEditorDialog()
        dialog.arguments = fragmentBundle
        dialog.showDialog(supportFragmentManager, this)
    }

    override fun onEditorSaveButtonClick(editedConfigKey: String, editedConfigValue: String) {
        if (!TextUtils.isEmpty(editedConfigKey)) {
            val editor = sharedPref.edit()
            try {
                when (sharedPrefValueType) {
                    TYPE_LONG -> editor.putLong(editedConfigKey, editedConfigValue.toLong()).apply()
                    TYPE_INT -> editor.putInt(editedConfigKey, editedConfigValue.toInt()).apply()
                    TYPE_FLOAT -> editor.putFloat(editedConfigKey, editedConfigValue.toFloat()).apply()
                    TYPE_BOOLEAN -> editor.putBoolean(editedConfigKey, editedConfigValue.toBoolean()).apply()
                    TYPE_STRING_SET -> editor.putStringSet(editedConfigKey, editedConfigValue.split("#").toSet()).apply()
                    else -> editor.putString(editedConfigKey, editedConfigValue).apply()
                }
                updateListAdapterData()
            } catch (e: Exception) {
                Toast.makeText(this, "Data Type is not correct", Toast.LENGTH_SHORT).show()
            }
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
        sharedPref = this.getSharedPreferences(
            intent.extras?.getString(DeveloperOptionActivity.SHARED_PREF_FILE),
            Context.MODE_PRIVATE
        )
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
