package com.tokopedia.developer_options.mssdk

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.tokopedia.developer_options.StringIntIntPair
import com.tokopedia.developer_options.mssdk.MsSdkOptionConst.DEFAULT_COLLECT_MODE
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.InputStream
import java.io.OutputStream

val Context.dataStore by dataStore(
fileName = "string_int_pair.pb",
serializer = StringIntPairSerializer
)

object StringIntPairSerializer : Serializer<StringIntIntPair> {
    override val defaultValue: StringIntIntPair = StringIntIntPair.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): StringIntIntPair {
        try {
            return StringIntIntPair.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: StringIntIntPair, output: OutputStream) = t.writeTo(output)
}

suspend fun saveStringIntPair(context: Context, value: StringIntIntPair) {
    context.dataStore.updateData { currentPreferences ->
        currentPreferences.toBuilder()
            .setStringValue(value.stringValue)
            .setIntValue(value.intValue)
            .build()
    }
}

suspend fun getStringIntPair(context: Context) =
    context.dataStore.data.first()

fun getStringIntPairBlock(context: Context) =
    runBlocking {context.dataStore.data.first()}

fun saveStringIntPairBlocking(context: Context, value: StringIntIntPair) {
    runBlocking {
        context.dataStore.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setStringValue(value.stringValue)
                .setIntValue(value.intValue)
                .build()
        }
    }
}
fun saveStringIntPairDefaultBlocking(context: Context) {
    runBlocking {
        val (defaultCollectString, defaultCollectIndex) = DEFAULT_COLLECT_MODE
        context.dataStore.updateData { currentPreferences ->
            currentPreferences.toBuilder()
                .setStringValue(defaultCollectString)
                .setIntValue(defaultCollectIndex)
                .setIntValue(0)
                .build()
        }
    }
}





