package com.tokopedia.favorite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.favorite.view.FragmentFavorite
import com.tokopedia.favorite.test.R

class InstrumentationFavoriteTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_test)

        val favoriteFragment: Fragment = FragmentFavorite.newInstance()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container_favorite, favoriteFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}