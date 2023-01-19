package com.tokopedia.sellerpersona.view.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.sellerpersona.databinding.ActivitySellerPersonaBinding

/**
 * Created by @ilhamsuaib on 17/01/23.
 */

class SellerPersonaActivity : BaseActivity() {

    private var binding: ActivitySellerPersonaBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView()
        setupView()
    }

    private fun setContentView() {
        binding = ActivitySellerPersonaBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
    }

    private fun setupView() {
        binding?.headerSellerPersona?.let {
            setSupportActionBar(it)
        }
    }
}