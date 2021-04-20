package com.tokopedia.officialstore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.officialstore.category.presentation.fragment.OfficialHomeContainerFragment

/**
 * Created by Lukas on 27/10/20.
 */
class OfficialStoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_official_store)
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, OfficialHomeContainerFragment.newInstance(null), "wishlist")
                .commit()
    }
}