package com.tokopedia.developer_options.hansel

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.hansel.data.HanselPatchData
import com.tokopedia.developer_options.hansel.uimodel.HanselUiModel
import com.tokopedia.unifycomponents.SearchBarUnify

class HanselActivity : AppCompatActivity() {

    private var hanselAdapter: HanselAdapter? = null

    private val hanselPatchData = mutableListOf<HanselUiModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.tokopedia.developer_options.R.layout.activity_hansel_list)
        initToolbar()
        initSearchBar()
        initData()
        initView()
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.hansel_patch_list)
        toolbar.subtitle = getString(R.string.tokopedia_name)
        setSupportActionBar(toolbar)
    }

    private fun initSearchBar() {
        val searchbar = findViewById<SearchBarUnify>(R.id.searchbar_hansel)
        searchbar.searchBarTextField.addTextChangedListener {
            hanselAdapter?.filter?.filter(it.toString())
        }
    }

    private fun initView() {
        val rv = findViewById<RecyclerView>(com.tokopedia.developer_options.R.id.rv_hansel_list)
        hanselAdapter = HanselAdapter(hanselPatchData.toList())
        rv.adapter = hanselAdapter
        rv.layoutManager = LinearLayoutManager(this)
    }

    private fun initData() {
        val prefsHanselCodePatch = getSharedPreferences(PREF_NAME_HANSEL_CODE_PATCH, Context.MODE_PRIVATE)
        val prefsHansel = getSharedPreferences(PREF_NAME_HANSEL, Context.MODE_PRIVATE)
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                prefsHanselCodePatch.all.values.forEach { value ->
                    value?.let { json ->
                        if (json is String) {
                            val mapPatchData = mutableMapOf<String, Int>()
                            val jsonPatchData = prefsHansel.getString(KEY_PATCH_USAGE_DATA, "") ?: ""
                            if (jsonPatchData.isNotEmpty()) {
                                mapPatchData.putAll(Gson().fromJson(jsonPatchData, object: TypeToken<Map<String, Int>>(){}.type))

                            }
                            val data = HanselPatchData.fromJson(json)
                            val counter = mapPatchData[data.patchId.toString()] ?: 0
                            hanselPatchData.add(data.toHanselUiModel(counter))
                        }
                    }
                }


            }
        } catch(e: Exception) {
            Log.e("HanselActivity", e.localizedMessage)
        }

    }

    companion object {
        private const val PREF_NAME_HANSEL_CODE_PATCH = "PREFERENCE_NAME_HANSEL_CODE_PATCH_PRIMARY"
        private const val PREF_NAME_HANSEL = "PREFERENCE_NAME_HANSEL"
        private const val KEY_PATCH_USAGE_DATA = "KEY_PATCH_USAGE_DATA"
    }
}
