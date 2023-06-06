package com.tokopedia.user.session.datastore

import androidx.datastore.core.Serializer
import com.tokopedia.encryption.security.AeadEncryptor
import com.tokopedia.user.session.UserSessionProto
import java.io.InputStream
import java.io.OutputStream

class UserSessionSerializer(private val aead: AeadEncryptor) : Serializer<UserSessionProto> {

    override val defaultValue: UserSessionProto
        get() = UserSessionProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserSessionProto {
        val encryptedInput = input.readBytes()
        val decryptedInput = if (encryptedInput.isNotEmpty()) {
            aead.getAead().decrypt(encryptedInput, null)
        } else {
            encryptedInput
        }
        return UserSessionProto.parseFrom(decryptedInput)
    }

    override suspend fun writeTo(t: UserSessionProto, output: OutputStream) {
        val byteArray = t.toByteArray()
        val encryptedBytes = aead.getAead().encrypt(byteArray, null)
        output.write(encryptedBytes)
    }
}
