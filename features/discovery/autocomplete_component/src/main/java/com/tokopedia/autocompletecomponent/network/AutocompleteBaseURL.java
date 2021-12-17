package com.tokopedia.autocompletecomponent.network;

import com.tokopedia.url.TokopediaUrl;

public class AutocompleteBaseURL {
    public static class Ace {
        public static String ACE_DOMAIN = TokopediaUrl.Companion.getInstance().getACE();
        public static final String PATH_INITIAL_STATE = "/initial-state/v1";
    }
}