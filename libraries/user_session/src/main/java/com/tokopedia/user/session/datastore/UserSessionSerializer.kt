package com.tokopedia.user.session.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.tokopedia.user.session.UserSessionProto
import com.tokopedia.user.session.datastore.UserSessionDataStoreImpl.Companion.DATA_STORE_FILE_NAME
import java.io.InputStream
import java.io.OutputStream
import kotlin.properties.ReadOnlyProperty

class UserSessionSerializer: Serializer<UserSessionProto> {
    override val defaultValue: UserSessionProto
            get() = UserSessionProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserSessionProto {
        try {
            return UserSessionProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserSessionProto, output: OutputStream) = t.writeTo(output)

    companion object {
        fun createDataStore(): ReadOnlyProperty<Context, DataStore<UserSessionProto>> {
            val sessionDataStore = dataStore(
                fileName = DATA_STORE_FILE_NAME,
                serializer = UserSessionSerializer()
            )
            return sessionDataStore
        }

    }
}

//object UserSessionSerializer : Serializer<UserSessionProto> {
//
//    override suspend fun readFrom(input: InputStream): UserSessionProto {
//        try {
//            return UserSessionProto.parseFrom(input)
//        } catch (exception: InvalidProtocolBufferException) {
//            throw CorruptionException("Cannot read proto.", exception)
//        }
//    }
//
//    override suspend fun writeTo(t: UserSessionProto, output: OutputStream) = t.writeTo(output)
//
//    override val defaultValue: UserSessionProto
//        get() = UserSessionProto.getDefaultInstance()
//}
