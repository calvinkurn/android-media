package com.tokopedia.developer_options.ab_test_rollence

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.FragmentActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.tokopedia.developer_options.R

import com.tokopedia.developer_options.ab_test_rollence.adapters.AbTestRollenceConfigListAdapter
import com.tokopedia.remoteconfig.abtest.AbTestPlatform

class AbTestRollenceConfigFragmentActivity : FragmentActivity(), AbTestRollenceConfigListener {

    companion object {
        const val ARGS_SELECTED_KEY = "selected_key"
        const val ARGS_SELECTED_KEY_VALUE = "selected_key_value"
    }

    private lateinit var listAdapter: AbTestRollenceConfigListAdapter
    private var abTestRollenceConfig: AbTestPlatform? = null
    private var isListEmpty: Boolean = false
    private var editTextSearchAbTestKey: AppCompatEditText? = null
    private var textAbTestRevision: AppCompatTextView? = null
    private val searchKeyword : String
        get() = editTextSearchAbTestKey?.text.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ab_test_rollence_editor)
        initView()
    }

    override fun onListItemClick(selectedConfigKey: String, selectedConfigKeyValue: String) {
        val fragmentBundle = Bundle()
        fragmentBundle.putString(ARGS_SELECTED_KEY, selectedConfigKey)
        fragmentBundle.putString(ARGS_SELECTED_KEY_VALUE, selectedConfigKeyValue)

        val dialog = AbTestRollenceConfigEditorDialog()
        dialog.arguments = fragmentBundle
        dialog.showDialog(supportFragmentManager, this)
    }

    override fun onEditorSaveButtonClick(editedConfigKey: String, editedConfigValue: String) {
        if (!TextUtils.isEmpty(editedConfigKey)) {
            abTestRollenceConfig?.setString(editedConfigKey, editedConfigValue)

            updateListAdapterData(searchKeyword)
        }
    }

    override fun onEditorDeleteKeyButtonClick(editedConfigKey: String) {
        if(editedConfigKey.isNotEmpty()){
            abTestRollenceConfig?.deleteKeyLocally(editedConfigKey)
        }
        updateListAdapterData(searchKeyword)
    }

    private fun getFilteredKeys(vararg prefixes: String): Set<String> {
        val keysSet: MutableSet<String> = mutableSetOf()

        abTestRollenceConfig?.let {
            for (prefix in prefixes) {
                keysSet.apply { addAll(it.getFilteredKeyByKeyName(prefix)) }
            }
        }

        if (keysSet.isEmpty()) {
            isListEmpty = true
        }

        return keysSet
    }

    private fun updateListAdapterData(prefix: String) {
        if (abTestRollenceConfig != null) {
            listAdapter.setConfigData(abTestRollenceConfig, getFilteredKeys(prefix))
        }
    }

    private fun initView() {
        abTestRollenceConfig = AbTestPlatform(this)
        listAdapter = AbTestRollenceConfigListAdapter(this)
        editTextSearchAbTestKey = findViewById(R.id.edit_text_search_ab_test_key)
        textAbTestRevision = findViewById(R.id.text_ab_test_revision)
        textAbTestRevision?.text = getString(
                R.string.ab_test_rollence_revision_version_prefix,
                abTestRollenceConfig?.getRevisionValue()
        )
        initListener()
        updateListAdapterData(searchKeyword)

        val rvConfigList: RecyclerView = findViewById(R.id.config_list_container)

        if (!isListEmpty) {
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val dividerItemDecoration = DividerItemDecoration(rvConfigList.context, layoutManager.orientation)
            rvConfigList.layoutManager = layoutManager
            rvConfigList.addItemDecoration(dividerItemDecoration)
            rvConfigList.adapter = listAdapter
        } else {
            rvConfigList.visibility = View.GONE
            findViewById<View>(R.id.empty_group).visibility = View.VISIBLE
        }
    }

    private fun initListener() {
        editTextSearchAbTestKey?.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                updateListAdapterData(text.toString())
            }

        })
    }
}
