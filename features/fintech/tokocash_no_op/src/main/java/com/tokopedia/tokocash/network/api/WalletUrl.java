package com.tokopedia.tokocash.network.api;

/**
 * Created by nabillasabbaha on 1/17/18.
 */

public interface WalletUrl {

    class BaseUrl {
        public static String ACCOUNTS_DOMAIN = "https://accounts.tokopedia.com/";
        public static String WALLET_DOMAIN = "https://www.tokocash.com/";
        public static String WEB_DOMAIN = "https://www.tokpedia.com/";
        public static String GQL_TOKOCASH_DOMAIN = "https://gql.tokopedia.com/";
    }

    class KeyHmac {
        public static String HMAC_PENDING_CASHBACK = "CPAnAGpC3NIg7ZSj";
    }
}
