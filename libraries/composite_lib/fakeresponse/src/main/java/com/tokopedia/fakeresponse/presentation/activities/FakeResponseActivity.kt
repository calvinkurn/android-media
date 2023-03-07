package com.tokopedia.fakeresponse.presentation.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.tokopedia.fakeresponse.R
import com.tokopedia.fakeresponse.presentation.fragments.DownloadFragment
import com.tokopedia.fakeresponse.presentation.fragments.FakeResponseFragment

class FakeResponseActivity : BaseActivity() {

    companion object {

        fun newInstance(context: Context): Intent {
            return Intent(
                context,
                FakeResponseActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    lateinit var fm: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fake_activity_fake_response)

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
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        }
    }

    override fun finish() {
        super.finish()
    }

}
