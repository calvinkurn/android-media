package com.tokopedia.dropoff.ui.autocomplete

import android.os.Bundle
import android.view.MenuItem
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.dropoff.databinding.ActivityAutocompleteBinding
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable

class AutoCompleteActivity : BaseActivity() {

    private var binding: ActivityAutocompleteBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAutocompleteBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(
            getIconUnifyDrawable(this, IconUnify.CLOSE)
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
