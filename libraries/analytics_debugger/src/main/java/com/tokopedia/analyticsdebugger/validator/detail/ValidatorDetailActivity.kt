package com.tokopedia.analyticsdebugger.validator.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.analyticsdebugger.R

class ValidatorDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validator_detail)

        val exp = intent.getStringExtra(EXTRA_EXPECTED)
        val act = intent.getStringExtra(EXTRA_ACTUAL)

        findViewById<TextView>(R.id.tv_expected).text = exp
        findViewById<TextView>(R.id.tv_actual).text = act
    }

    companion object {
        private const val EXTRA_EXPECTED = "EXTRA_EXPECTED"
        private const val EXTRA_ACTUAL = "EXTRA_ACTUAL"

        fun newIntent(context: Context, expected: String, actual: String) =
                Intent(context, ValidatorDetailActivity::class.java).apply {
                    putExtra(EXTRA_EXPECTED, expected)
                    putExtra(EXTRA_ACTUAL, actual)
                }
    }
}
