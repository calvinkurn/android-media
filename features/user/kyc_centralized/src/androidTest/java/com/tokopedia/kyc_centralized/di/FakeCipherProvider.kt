package com.tokopedia.kyc_centralized.di

import com.tokopedia.kyc_centralized.util.CipherProvider
import javax.crypto.Cipher

class FakeCipherProvider: CipherProvider {
    override fun initAesEncrypt(): Cipher {
        TODO("Not yet implemented")
    }

    override fun initAesDecrypt(tempIv: ByteArray?): Cipher {
        TODO("Not yet implemented")
    }
}