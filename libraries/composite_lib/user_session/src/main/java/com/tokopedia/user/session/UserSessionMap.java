package com.tokopedia.user.session;

import android.util.Pair;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class UserSessionMap {
    public static ConcurrentHashMap<Pair<String, String>, Object> map = new ConcurrentHashMap<>();
}
