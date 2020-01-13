package com.rahullohra.fakeresponse.domain.repository

import java.net.URL

class RemoteSqliteRepository : BaseRepository {

    init {
        prepareMap()
    }

    companion object AbiEndpoints {
        const val BASE_URL = "https://raw.githubusercontent.com/"
        const val DIRECTORY_URL = "${BASE_URL}EXALAB/sqlite3-android/master/binary/"
        const val sqlite3 = "/sqlite3"
        const val x86 = "${DIRECTORY_URL}/x86${sqlite3}"
        const val x86_64 = "${DIRECTORY_URL}/x86-64${sqlite3}"
        const val arm64_v8a = "${DIRECTORY_URL}/arm64-v8a${sqlite3}"
        const val armeabi_v7a = "${DIRECTORY_URL}/armeabi-v7a${sqlite3}"
        const val armeabi = "${DIRECTORY_URL}/armeabi${sqlite3}"

        val map = HashMap<String, String>()
        fun prepareMap() {
            map["x86"] = x86
            map["x86-64"] = x86_64
            map["arm64-v8a"] = arm64_v8a
            map["armeabi-v7a"] = armeabi_v7a
            map["armeabi"] = armeabi
        }
    }


    fun getSqlite(cpu: String): ByteArray {
        return URL(map[cpu]).readBytes()
    }
}
