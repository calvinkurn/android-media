package com.tokopedia.tkpdcontent.analytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tokopedia.tkpdcontent.analytics.KolTracking.Event.PROMO_CLICK;
import static com.tokopedia.tkpdcontent.analytics.KolTracking.Event.PROMO_VIEW;

/**
 * @author by nisie on 1/2/18.
 */

public class KolTracking {

    private static final String EVENT = "event";
    private static final String ECOMMERCE = "ecommerce";

    public static class Event {

        static final String PROMO_VIEW = "promoView";
        static final String PROMO_CLICK = "promoClick";
    }

    public static class Ecommerce {

        private static final String PROMOTIONS = "promotions";

        private static final String KEY_ID = "id";
        private static final String KEY_NAME = "name";
        private static final String KEY_CREATIVE = "creative";
        private static final String KEY_POSITION = "position";
        private static final String KEY_CATEGORY = "category";
        private static final String KEY_PROMO_ID = "promo_id";
        private static final String KEY_PROMO_CODE = "promo_code";
        private static final String KEY_USER_ID = "userId";
        private static final String KEY_USER_ID_MOD = "userIdmodulo";


        public static Map<String, Object> getKolContentEcommerceView(List<Promotion> listPromotion) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(PROMO_VIEW, getListPromotions(listPromotion));
            return hashMap;
        }

        public static Map<String, Object> getKolContentEcommerceClick(List<Promotion> listPromotion) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(PROMO_CLICK, getListPromotions(listPromotion));
            return hashMap;
        }

        private static Map<String, Object> getListPromotions(List<Promotion> list) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(PROMOTIONS, createList(list));
            return hashMap;
        }

        private static List<Object> createList(List<Promotion> listPromotion) {
            List<Object> list = new ArrayList<>();
            for (Promotion promo : listPromotion) {
                Map<String, Object> map = createPromotionMap(promo);
                list.add(map);
            }
            return list;
        }

        private static Map<String, Object> createPromotionMap(Promotion promo) {
            Map<String, Object> map = new HashMap<>();
            map.put(KEY_ID, String.valueOf(promo.getId()));
            map.put(KEY_NAME, promo.getName());
            map.put(KEY_CREATIVE, promo.getCreative());
            map.put(KEY_POSITION, String.valueOf(promo.getPosition()));
            map.put(KEY_CATEGORY, promo.getCategory());
            map.put(KEY_PROMO_ID, String.valueOf(promo.getPromoId()));
            map.put(KEY_PROMO_CODE, promo.getPromoCode());
            map.put(KEY_USER_ID, promo.getUserId());
            map.put(KEY_USER_ID_MOD, promo.getUserIdMod50());
            return map;
        }

    }

    public static class Promotion {

        private static final String NAME_PROFILE_PAGE = "/profile page";
        private static final String KOL_POST = "kolpost";
        int id;
        String name;
        String creative;
        int position;
        String category;
        int promoId;
        String promoCode;
        String userId;
        String userIdMod50;

        public Promotion(int id, String name, String creative, int position, String category,
                         int promoId, String promoCode, int userId) {
            this.id = id;
            this.name = name;
            this.creative = creative;
            this.position = position;
            this.category = category;
            this.promoId = promoId;
            this.promoCode = promoCode;
            this.userId = String.valueOf(userId);
            this.userIdMod50 = String.valueOf(userId % 50);
        }

        /**
         * kolId
         */
        public int getId() {
            return id;
        }

        /**
         * kolId
         */
        public String getName() {
            return name;
        }

        public String getCreative() {
            return creative;
        }

        public int getPosition() {
            return position;
        }

        public String getCategory() {
            return category;
        }

        public int getPromoId() {
            return promoId;
        }

        public String getPromoCode() {
            return promoCode;
        }

        public String getUserId() {
            return userId;
        }

        public String getUserIdMod50() {
            return userIdMod50;
        }

        public static String createContentNameKolPost(String tagsType) {
            return NAME_PROFILE_PAGE + " - "
                    + KOL_POST + " - "
                    + tagsType;
        }
    }

    public static Map<String, Object> getKolImpressionTracking(List<Promotion> listPromotion) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(EVENT, PROMO_VIEW);
        hashMap.put(ECOMMERCE, Ecommerce.getKolContentEcommerceView(listPromotion));
        return hashMap;
    }

    public static Map<String, Object> getKolClickTracking(List<Promotion> listPromotion) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(EVENT, PROMO_CLICK);
        hashMap.put(ECOMMERCE, Ecommerce.getKolContentEcommerceClick(listPromotion));
        return hashMap;
    }
}
