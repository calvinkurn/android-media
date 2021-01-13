package com.tokopedia.ordermanagement.snapshot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tokopedia.ordermanagement.snapshot.ui.main.SnapshotFragment

class SnapshotActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.snapshot_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SnapshotFragment.newInstance())
                    .commitNow()
        }
    }
}