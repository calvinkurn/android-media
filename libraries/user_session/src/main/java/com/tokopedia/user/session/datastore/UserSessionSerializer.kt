package com.tokopedia.user.session.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.tokopedia.user.session.UserSessionProto
import java.io.InputStream
import java.io.OutputStream

object UserSessionSerializer : Serializer<UserSessionProto> {

    override suspend fun readFrom(input: InputStream): UserSessionProto {
        try {
            return UserSessionProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserSessionProto, output: OutputStream) = t.writeTo(output)

    override val defaultValue: UserSessionProto
        get() = UserSessionProto.getDefaultInstance()
}
