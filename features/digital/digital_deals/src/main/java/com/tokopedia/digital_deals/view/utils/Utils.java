package com.tokopedia.digital_deals.view.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.constant.TkpdCache;
import com.tokopedia.digital_deals.DealsModuleRouter;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.data.source.DealsUrl;
import com.tokopedia.digital_deals.view.model.CategoryItem;
import com.tokopedia.digital_deals.view.model.FilterItem;
import com.tokopedia.digital_deals.view.model.Location;
import com.tokopedia.digital_deals.view.model.Outlet;
import com.tokopedia.digital_deals.view.model.ValuesItem;
import com.tokopedia.digital_deals.view.model.response.DealsResponse;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


public class Utils {
    private static Utils singleInstance;
    private static Location location;
    public static String BRAND_QUERY_PARAM_TREE = "tree";
    public static String BRAND_QUERY_PARAM_BRAND = "brand";
    public static String QUERY_PARAM_CHILD_CATEGORY_ID = "child_category_ids";
    public static String QUERY_PARAM_CITY_ID = "cities";
    public static final String NEXT_URL = "nexturl";
    private static final float MAX_RADIUS = 25.0f;
    private static final float MIN_RADIUS = 0.0f;
    public static Locale locale = new Locale("in", "ID");
    private static final String RUPIAH_FORMAT = "Rp %s";
    private SparseIntArray likedEventMap;
    private SparseIntArray unLikedEventMap;


    synchronized public static Utils getSingletonInstance() {
        if (singleInstance == null)
            singleInstance = new Utils();
        return singleInstance;
    }

    private Utils() {
    }

    public void addLikedEvent(int id, int currentLikes) {
        if (likedEventMap == null)
            likedEventMap = new SparseIntArray();
        likedEventMap.put(id, currentLikes + 1);
        removeUnlikedEvent(id);
    }

    public void removeLikedEvent(int id) {
        if (likedEventMap != null && likedEventMap.size() > 0) {
            int likes = likedEventMap.get(id);
            likedEventMap.delete(id);
            addUnlikedEvent(id, likes);
        }
    }

    public int containsLikedEvent(int id) {
        if (likedEventMap != null && likedEventMap.size() > 0)
            return likedEventMap.get(id, -1);
        return -1;
    }

    public int containsUnlikedEvent(int id) {
        if (unLikedEventMap != null && unLikedEventMap.size() > 0)
            return unLikedEventMap.get(id, -1);
        return -1;
    }

    private void addUnlikedEvent(int id, int likes) {
        if (unLikedEventMap == null)
            unLikedEventMap = new SparseIntArray();
        unLikedEventMap.put(id, likes - 1);
    }

    private void removeUnlikedEvent(int id) {
        if (unLikedEventMap != null && unLikedEventMap.size() > 0) {
            unLikedEventMap.delete(id);
        }

    }

    public ArrayList<CategoryItem> convertIntoCategoryListViewModel(DealsResponse dealsResponse) {

        ArrayList<CategoryItem> categoryRespons = new ArrayList<>();
        if (dealsResponse.getHome() != null && dealsResponse.getCategoryItems() != null) {
            for (CategoryItem categoryItem : dealsResponse.getCategoryItems()) {

                CategoryItem category = new CategoryItem();
                category.setTitle(categoryItem.getTitle());
                category.setCategoryId(categoryItem.getCategoryId());
                category.setCount(categoryItem.getCount());
                category.setName(categoryItem.getName());
                category.setMediaUrl(categoryItem.getMediaUrl());
                category.setCategoryUrl(categoryItem.getCategoryUrl());
                category.setUrl(categoryItem.getUrl());
                category.setItems(categoryItem.getItems());
                category.setIsCard(categoryItem.getIsCard());

                switch (categoryItem.getName().toLowerCase()) {
                    case "top":
                        categoryRespons.add(0, category);
                        break;
                    case "carousel":
                        categoryRespons.add(0, category);
                        break;
                    default:
                        categoryRespons.add(category);
                        break;
                }
            }

            applyFilterOnCategories(categoryRespons, dealsResponse.getFilters());


        }
        return categoryRespons;
    }

    private void applyFilterOnCategories(ArrayList<CategoryItem> categoryRespons, List<FilterItem> filters) {
        Map<Integer, Integer> sortOrder = new HashMap<>();
        if (filters != null) {
            if (categoryRespons.get(0).getCategoryId() == categoryRespons.get(1).getCategoryId()) {
                categoryRespons.get(1).setCategoryId(-1);            //Since carousel and top have same id's
            }
            sortOrder.put(categoryRespons.get(0).getCategoryId(), -1);   //dummy for top or carousel
            sortOrder.put(categoryRespons.get(1).getCategoryId(), -2);   //dummy for top or carousel
            for (FilterItem filter : filters) {
                if (filter.getAttributeName().equals("child_category_ids")) {
                    if (filter.getValues() != null) {
                        for (ValuesItem value : filter.getValues()) {
                            sortOrder.put(value.getId(), value.getPriority());
                        }
                    }
                    if (sortOrder.size() == categoryRespons.size()) {
                        Collections.sort(categoryRespons, new CategoryItemComparator(sortOrder));
                    }
                }
            }
        }
    }

    public void sortOutletsWithLocation(List<Outlet> outlets, Location location) {
        if (location == null || outlets == null)
            return;
        List<Outlet> outlets1 = new ArrayList<>();
        List<Outlet> outlets2 = new ArrayList<>();
        for (Outlet outlet : outlets) {

            if (outlet.getLocationId() == location.getId()) {
                outlets1.add(outlet);
            } else {
                if (!TextUtils.isEmpty(outlet.getSearchName())
                        && !TextUtils.isEmpty(location.getSearchName())
                        && outlet.getSearchName().equalsIgnoreCase(location.getSearchName()))
                    outlets1.add(outlet);
                else {
                    outlets2.add(outlet);
                }
            }
        }
        outlets.clear();
        outlets.addAll(outlets1);
        outlets.addAll(outlets2);

    }

    private class CategoryItemComparator implements Comparator<CategoryItem> {
        private Map<Integer, Integer> sortOrder;

        public CategoryItemComparator(Map<Integer, Integer> sortOrder) {
            this.sortOrder = sortOrder;
        }

        @Override
        public int compare(CategoryItem i1, CategoryItem i2) {
            Integer id1 = sortOrder.get(i1.getCategoryId());
            if (id1 == null) {
                throw new IllegalArgumentException("Bad id encountered: " +
                        i1.getCategoryId());
            }
            Integer id2 = sortOrder.get(i2.getCategoryId());
            if (id2 == null) {
                throw new IllegalArgumentException("Bad id encountered: " +
                        i2.getCategoryId());
            }
            return id1.compareTo(id2);
        }
    }

    public static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }

    public static String convertEpochToString(int time) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy", new Locale("in", "ID", ""));
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        Long epochTime = time * 1000L;
        Date date = new Date(epochTime);
        String dateString = sdf.format(date);
        return dateString;
    }

    public static String convertToCurrencyString(long value) {
        return String.format(RUPIAH_FORMAT, NumberFormat.getNumberInstance(locale).format(value));
    }

    public Location getLocation(Context context) {

        if (location == null) {
            final LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.DEALS_LOCATION);
            String locationjson = localCacheHandler.getString(TkpdCache.Key.KEY_DEALS_LOCATION, null);
            if (locationjson != null) {
                Gson gson = new Gson();
                location = gson.fromJson(locationjson, Location.class);
            }
        }
        return location;
    }

    public void updateLocation(Context context, Location locatn) {

        location = locatn;
        final LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.DEALS_LOCATION);
        Gson gson = new Gson();
        String json = gson.toJson(location);
        localCacheHandler.putString(TkpdCache.Key.KEY_DEALS_LOCATION, json);
        localCacheHandler.applyEditor();
    }

    public void openGoogleMapsActivity(Context context, String latLng) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + latLng);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.cannot_find_application), Toast.LENGTH_SHORT).show();
        }
    }

    public void showSnackBarDeals(String text, Context context, ViewGroup coordinatorLayout, boolean locationToast) {
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

        TextView textView = layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        LayoutInflater inflater = LayoutInflater.from(context);
        View snackView = inflater.inflate(R.layout.custom_location_change_snackbar, null);
        TextView tvmsg = snackView.findViewById(R.id.tv_msg);
        if (locationToast) {
            String str=context.getResources().getString(R.string.location_changed_to);
            str+=text.toUpperCase();
            tvmsg.setText(getLocationText(str,context.getResources().getColor(R.color.black_40)));
        } else {
            snackView.findViewById(R.id.main_content).setBackgroundColor(context.getResources().getColor(R.color.red_50));
            snackView.findViewById(R.id.divider).setBackgroundColor(context.getResources().getColor(R.color.red_error));
            tvmsg.setText(text);
        }

        TextView okbtn = snackView.findViewById(R.id.snack_ok);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        layout.addView(snackView, 0);
        layout.setPadding(0, 0, 0, 0);
        snackbar.show();
    }

    private SpannableString getLocationText(String text, int color) {
        int startIndexOfLink = text.indexOf(":");
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), startIndexOfLink, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(color), startIndexOfLink, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
    public void shareDeal(String deeplinkSlug, Context context, String name, String imageUrl, String desktopUrl) {
        String uri = DealsUrl.AppLink.DIGITAL_DEALS + "/" + deeplinkSlug;
        ((DealsModuleRouter) ((Activity) context).getApplication()).shareDeal(context, uri, name, imageUrl, desktopUrl);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public Bitmap setBlur(Bitmap smallBitmap, float radius, Context context) {
        if (radius > MIN_RADIUS && radius <= MAX_RADIUS) {
            Bitmap inputBitmap = Bitmap.createScaledBitmap(smallBitmap, smallBitmap.getWidth(), smallBitmap.getHeight(), false);
            Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

            RenderScript renderScript = RenderScript.create(context);
            ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
            Allocation tmpIn = Allocation.createFromBitmap(renderScript, inputBitmap);
            Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);
            theIntrinsic.setRadius(radius);
            theIntrinsic.setInput(tmpIn);
            theIntrinsic.forEach(tmpOut);
            tmpOut.copyTo(outputBitmap);

            return outputBitmap;
        } else {
            return smallBitmap;
        }
    }

    public static String fetchOrderId(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

}
