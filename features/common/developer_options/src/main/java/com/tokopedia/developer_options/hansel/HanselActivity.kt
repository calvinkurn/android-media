package com.tokopedia.developer_options.hansel

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HanselActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.tokopedia.developer_options.R.layout.activity_hansel_list)
        initView()
    }

    private fun initView() {
        val prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        val rv = findViewById<RecyclerView>(com.tokopedia.developer_options.R.id.rv_hansel_list)
        rv.adapter = HanselAdapter(listOf())
        rv.layoutManager = LinearLayoutManager(this)
    }

    companion object {
        private const val PREF_NAME = "PREFERENCE_NAME_HANSEL_CODE_PATCH_PRIMARY"
    }
}
