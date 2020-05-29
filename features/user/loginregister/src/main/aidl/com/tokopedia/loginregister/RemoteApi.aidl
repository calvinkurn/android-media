package com.tokopedia.loginregister;

interface RemoteApi {
    void getDummyKey(String taskId);
    void getUserProfile(String taskId);
}