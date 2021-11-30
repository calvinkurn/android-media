package com.tokopedia.tkpd

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import java.net.UnknownHostException

class ErrorMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error_message)
        initView()
    }

    private fun initView() {
        findViewById<Button>(R.id.button_npe).setOnClickListener {
            showToaster(NullPointerException())
        }

        findViewById<Button>(R.id.button_ioob).setOnClickListener {
            showToaster(IndexOutOfBoundsException())
        }

        findViewById<Button>(R.id.button_number).setOnClickListener {
            showToaster(NumberFormatException())
        }

        findViewById<Button>(R.id.button_unknown_host).setOnClickListener {
            showToaster(UnknownHostException())
        }

        findViewById<Button>(R.id.button_message_403).setOnClickListener {
            showToaster(MessageErrorException("Error nih", "403"))
        }

        findViewById<Button>(R.id.button_message_504).setOnClickListener {
            showToaster(MessageErrorException("Error lain", "504"))
        }
    }

    private fun showToaster(e: Throwable) {
        var textToast = ErrorHandler.getErrorMessage(this, e)
        val snackbar = Snackbar.make(findViewById(android.R.id.content), textToast, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("Tutup") {
            snackbar.dismiss()
        }
        snackbar.show()
    }
}