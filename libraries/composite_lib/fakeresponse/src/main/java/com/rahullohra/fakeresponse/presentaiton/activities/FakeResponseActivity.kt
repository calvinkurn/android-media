package com.rahullohra.fakeresponse.presentaiton.activities

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rahullohra.fakeresponse.R
import com.rahullohra.fakeresponse.presentaiton.fragments.DownloadFragment
import com.rahullohra.fakeresponse.presentaiton.fragments.FakeResponseFragment

class FakeResponseActivity : BaseActivity() {

    lateinit var fm: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fake_response)

        fm = findViewById(R.id.fm)

        if (savedInstanceState == null) {
            showDownloadFragment()
        }
    }

    fun showGqlFragment() {
        showFragment(FakeResponseFragment.newInstance())
    }

    fun showDownloadFragment() {
        showFragment(DownloadFragment.newInstance())
    }

    fun onSqlFilesArePresent() {
        showGqlFragment()
    }

    fun showFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()

        supportFragmentManager
            .beginTransaction()
            .add(fm.id, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(supportFragmentManager.backStackEntryCount == 0){
            finish()
        }
    }

}
