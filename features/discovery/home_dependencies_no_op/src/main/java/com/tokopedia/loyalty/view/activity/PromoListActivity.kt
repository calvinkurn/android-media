package com.tokopedia.loyalty.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PromoListActivity: AppCompatActivity() {

    companion object {
        const val DEFAULT_AUTO_SELECTED_CATEGORY_ID = "0"
        const val DEFAULT_AUTO_SELECTED_MENU_ID = "0"

        @JvmStatic
        fun newInstance(context: Context, menuId: String, categoryId: String): Intent {
            return Intent(context, PromoListActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        Toast.makeText(this, "no-op PromoListActivity", Toast.LENGTH_SHORT).show()

        finish()
    }
}