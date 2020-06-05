package com.example.googletagmanagerwithannotation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics

@SuppressLint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseAnalytics.Param.ITEM_ID
    }

    infix fun Bundle.isEqualTo(that: Bundle): Boolean {
        if (that.size() != this.size() || !that.keySet().containsAll(this.keySet())) {
            return false
        }

        this.keySet().forEach {
            if (this[it] is Bundle) return this[it] as Bundle isEqualTo that[it] as Bundle
        }

        return true
    }
}