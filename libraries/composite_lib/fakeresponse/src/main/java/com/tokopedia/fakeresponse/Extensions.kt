package com.tokopedia.fakeresponse

import android.app.Activity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Toast
import com.tokopedia.fakeresponse.data.models.Either
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

fun Activity.toast(message: String?) {
    if (!TextUtils.isEmpty(message))
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
}

interface FakeResponseTextWatcher : TextWatcher {
    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}

object Extensions {
    fun isJSONValid(test: String): Either<Exception, Boolean> {
        try {
            JSONObject()
        } catch (ex: JSONException) {
            JSONArray(test)
        } catch (ex1: JSONException) {
            return Either.Left(ex1)
        }
        return Either.Right(true)
    }
}

const val TRANSACTION = "transaction"
const val GQL_RECORD = "gqlRecord"
const val REST_RECORD = "restRecord"
