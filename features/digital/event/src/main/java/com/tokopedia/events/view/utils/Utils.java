package com.tokopedia.events.view.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.events.R;
import com.tokopedia.events.domain.model.EventsCategoryDomain;
import com.tokopedia.events.domain.model.EventsItemDomain;
import com.tokopedia.events.domain.model.LikeUpdateResultDomain;
import com.tokopedia.events.domain.model.request.likes.LikeUpdateModel;
import com.tokopedia.events.domain.model.request.likes.Rating;
import com.tokopedia.events.domain.postusecase.PostUpdateEventLikesUseCase;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.CategoryViewModel;
import com.tokopedia.events.view.viewmodel.SearchViewModel;
import com.tokopedia.user.session.UserSession;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pranaymohapatra on 10/01/18.
 */

public class Utils {
    private static Utils singleInstance;
    private List<CategoryItemsViewModel> topEvents;
    private HashSet<Integer> likedEventSet;
    private HashSet<Integer> unLikedEventSet;

    synchronized public static Utils getSingletonInstance() {
        if (singleInstance == null)
            singleInstance = new Utils();
        return singleInstance;
    }

    private Utils() {
        Log.d("UTILS", "Utils Instance created");
    }

    public HashSet<Integer> getLikedEventSet() {
        return likedEventSet;
    }

    public void addLikedEvent(int id) {
        if (likedEventSet == null) {
            likedEventSet = new HashSet<>();
        }
        likedEventSet.add(id);

    }

    public void removeLikedEvent(int id) {
        if (likedEventSet != null && !likedEventSet.isEmpty())
            likedEventSet.remove(id);
    }

    public boolean containsLikedEvent(int id) {
        return likedEventSet != null && !likedEventSet.isEmpty() && likedEventSet.contains(id);
    }

    public void addUnlikedEvent(int id) {
        if (unLikedEventSet == null)
            unLikedEventSet = new HashSet<>();
        unLikedEventSet.add(id);
    }

    public void removeUnlikedEvent(int id) {
        if (unLikedEventSet != null && !unLikedEventSet.isEmpty())
            unLikedEventSet.remove(id);
    }

    public boolean containsUnlikeEvent(int id) {
        if (unLikedEventSet != null && !unLikedEventSet.isEmpty() && unLikedEventSet.contains(id)) {
            removeUnlikedEvent(id);
            return true;
        }
        return false;
    }

    public List<CategoryViewModel> convertIntoCategoryListVeiwModel(List<EventsCategoryDomain> categoryList) {
        List<CategoryViewModel> categoryViewModels = new ArrayList<>();
        if (categoryList != null) {
            for (EventsCategoryDomain eventsCategoryDomain : categoryList
                    ) {
                if ("top".equalsIgnoreCase(eventsCategoryDomain.getName())) {
                    categoryViewModels.add(0, new CategoryViewModel(eventsCategoryDomain.getTitle(),
                            eventsCategoryDomain.getName(),
                            eventsCategoryDomain.getMediaURL(),
                            convertIntoCategoryListItemsVeiwModel(eventsCategoryDomain.getItems())));

                } else {
                    CategoryViewModel model = new CategoryViewModel(eventsCategoryDomain.getTitle(),
                            eventsCategoryDomain.getName(),
                            eventsCategoryDomain.getMediaURL(),
                            convertIntoCategoryListItemsVeiwModel(eventsCategoryDomain.getItems()));
                    model.setCategoryId(eventsCategoryDomain.getId());
                    categoryViewModels.add(model);

                }

            }
        }
        return categoryViewModels;
    }

    public List<CategoryItemsViewModel> convertIntoCategoryListItemsVeiwModel(List<EventsItemDomain> categoryResponseItemsList) {
        List<CategoryItemsViewModel> categoryItemsViewModelList = new ArrayList<>();
        if (categoryResponseItemsList != null) {
            CategoryItemsViewModel CategoryItemsViewModel;
            for (EventsItemDomain categoryEntity : categoryResponseItemsList) {
                CategoryItemsViewModel = new CategoryItemsViewModel();
                CategoryItemsViewModel.setId(categoryEntity.getId());
                CategoryItemsViewModel.setCategoryId(categoryEntity.getCategoryId());
                CategoryItemsViewModel.setChildCategoryIds(categoryEntity.getChildCategoryIds());
                CategoryItemsViewModel.setDisplayName(categoryEntity.getDisplayName());
                CategoryItemsViewModel.setTitle(categoryEntity.getTitle());
                CategoryItemsViewModel.setImageApp(categoryEntity.getImageApp());
                CategoryItemsViewModel.setThumbnailApp(categoryEntity.getThumbnailApp());
                CategoryItemsViewModel.setSalesPrice(categoryEntity.getSalesPrice());
                CategoryItemsViewModel.setMinStartTime(categoryEntity.getMinStartTime());
                CategoryItemsViewModel.setCityName(categoryEntity.getCityName());
                CategoryItemsViewModel.setMinStartDate(categoryEntity.getMinStartDate());
                CategoryItemsViewModel.setMaxEndDate(categoryEntity.getMaxEndDate());
                CategoryItemsViewModel.setLongRichDesc(categoryEntity.getLongRichDesc());
                CategoryItemsViewModel.setDisplayTags(categoryEntity.getDisplayTags());
                CategoryItemsViewModel.setTnc(categoryEntity.getTnc());
                CategoryItemsViewModel.setIsTop(categoryEntity.getIsTop());
                CategoryItemsViewModel.setHasSeatLayout(categoryEntity.getHasSeatLayout());
                CategoryItemsViewModel.setUrl(categoryEntity.getUrl());
                CategoryItemsViewModel.setSeoUrl(categoryEntity.getSeoUrl());
                CategoryItemsViewModel.setMinLikes(categoryEntity.getLikes());
                if (categoryEntity.isLiked())
                    Utils.getSingletonInstance().addLikedEvent(categoryEntity.getId());
                CategoryItemsViewModel.setWasLiked(categoryEntity.isLiked());
                CategoryItemsViewModel.setLiked(categoryEntity.isLiked());
                categoryItemsViewModelList.add(CategoryItemsViewModel);
            }
        }
        return categoryItemsViewModelList;
    }

    public ArrayList<CategoryItemsViewModel> convertIntoSearchViewModel(List<CategoryViewModel> source) {
        ArrayList<CategoryItemsViewModel> searchViewModels = new ArrayList<>();
        if (source != null) {
            for (CategoryViewModel item : source) {
                if (item.getItems() != null) {
                    List<CategoryItemsViewModel> sourceModels = item.getItems();
                    for (CategoryItemsViewModel sourceItem : sourceModels) {
                        if (sourceItem.getIsTop() == 1) {
                            searchViewModels.add(sourceItem);
                        }
                    }
                }

            }
        }
        return searchViewModels;
    }

    private boolean isPresent(ArrayList<SearchViewModel> searchViewModels, String title) {
        for (SearchViewModel viewModel : searchViewModels) {
            if (viewModel.getTitle().equals(title))
                return true;
        }
        return false;
    }

    public List<SearchViewModel> convertSearchResultsToModel(List<CategoryItemsViewModel> categoryItemsViewModels) {
        List<SearchViewModel> searchResults = null;
        if (categoryItemsViewModels != null && !categoryItemsViewModels.isEmpty()) {
            SearchViewModel searchModelItem;
            searchResults = new ArrayList<>();
            for (CategoryItemsViewModel sourceItem : categoryItemsViewModels) {
                searchModelItem = new SearchViewModel();
                searchModelItem.setCityName(sourceItem.getCityName());
                searchModelItem.setDisplayName(sourceItem.getDisplayName());
                searchModelItem.setImageApp(sourceItem.getImageApp());
                searchModelItem.setMaxEndDate(sourceItem.getMaxEndDate());
                searchModelItem.setMinStartDate(sourceItem.getMinStartDate());
                searchModelItem.setSalesPrice(sourceItem.getSalesPrice());
                searchModelItem.setTitle(sourceItem.getTitle());
                searchModelItem.setUrl(sourceItem.getUrl());
                searchResults.add(searchModelItem);
            }
        }
        return searchResults;
    }

    public void setEventLike(Context context, CategoryItemsViewModel model, PostUpdateEventLikesUseCase postUpdateEventLikesUseCase,
                             Subscriber<LikeUpdateResultDomain> subscriber) {
        LikeUpdateModel requestModel = new LikeUpdateModel();
        Rating rating = new Rating();
        if (model.isLiked()) {
            rating.setIsLiked("false");
            model.setLiked(false);
        } else {
            rating.setIsLiked("true");
            model.setLiked(true);
        }
        rating.setUserId(Integer.parseInt(Utils.getUserSession(context).getUserId()));
        rating.setProductId(model.getId());
        rating.setFeedback("");
        requestModel.setRating(rating);
        com.tokopedia.usecase.RequestParams requestParams = com.tokopedia.usecase.RequestParams.create();
        requestParams.putObject("request_body", requestModel);
        postUpdateEventLikesUseCase.createObservable(requestParams).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

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

    public void setCalendar(TextView textView, String[] dateArray) {
        SpannableString date = new SpannableString(dateArray[1]);
        SpannableString month = new SpannableString(dateArray[2]);
        date.setSpan(new AbsoluteSizeSpan(20, true), 0, date.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        month.setSpan(new AbsoluteSizeSpan(12, true), 0, month.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(TextUtils.concat(date, "\n", month));
    }

    public static List<String> getDisplayTags(String displayTag) {
        List<String> displayTagsList = new ArrayList<>();

        String[] temp = displayTag.split("\\|");
        for (int i = 0; i < temp.length; i++) {
            displayTagsList.add(temp[i]);
        }

        return displayTagsList;
    }


    public String convertEpochToString(int time) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMM yyyy", new Locale("in", "ID", ""));
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        Long epochTime = time * 1000L;
        Date date = new Date(epochTime);
        return sdf.format(date);
    }

    public String convertLongEpoch(long epoch) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd'/'MM'/'yy", new Locale("in", "ID", ""));
        Date date = new Date(epoch);
        return sdf.format(date);
    }

    public void setTopEvents(List<CategoryItemsViewModel> topEvents) {
        this.topEvents = topEvents;
    }

    public List<CategoryItemsViewModel> getTopEvents() {
        return this.topEvents;
    }

    public static String[] getDateArray(String dateRange) {
        String[] date = new String[3];
        String[] temp = dateRange.split(",");
        date[0] = temp[0];//day
        String[] temp2 = temp[1].split(" ");
        //Sat, 14 Apr 2018 - Sat, 14 Apr 2018
        date[1] = temp2[1];//date
        date[2] = temp2[2];//month
        return date;
    }

    private static boolean isNullOrEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isNotNullOrEmpty(String string) {
        return !isNullOrEmpty(string);
    }

    public static Bitmap getBitmap(Context context, LinearLayout v) {
        v.setDrawingCacheEnabled(true);
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Bitmap bmp = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        canvas.drawColor(ContextCompat.getColor(context, R.color.preview_bg));
        v.draw(canvas);
        return bmp;
    }

    public int convertDip2Pixels(Context context, int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
    }

    public static void saveImage(Context context, Bitmap finalBitmap) {


        String extStorageDirectory = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString();
        File folder = new File(extStorageDirectory, "store_image");
        if (!folder.exists())
            folder.mkdir();

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image" + n + ".png";

        File pdfFile = new File(folder, fname);

        try {
            pdfFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream out = new FileOutputStream(pdfFile);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void shareEvent(Context context, String title, String URL) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT,
                String.format(context.getResources().getString(R.string.check_this_out),
                        title));
        share.putExtra(Intent.EXTRA_TEXT, "https://www.tokopedia.com/events/detail/" + URL);
        context.startActivity(Intent.createChooser(share, "Share Event!"));
    }

    public static class Constants {
        public final static String BOOK = "book";
        public final static String EXTRA_EVENT_CALENDAR = "EVENTCALENDAR";
        public final static String THEMEPARK = "hiburan";
        public final static String PROMOURL = "https://www.tokopedia.com/promo/tiket/events/";
        public final static String FAQURL = "https://www.tokopedia.com/bantuan/faq-tiket-event/";
        public final static String TOP_EVENTS = "TOP EVENTS";
        public final static String CHECKOUTDATA = "checkoutdata";
        public final static String PRODUCTID = "product_id";
        public final static String TOPEVENTS = "TOPEVENTS";
        public final static String EVENTS = "events";
        public final static String HOMEDATA = "homedata";
        public final static String FAVOURITEDATA = "favouritedata";
        public static final int REVIEW_REQUEST = 1901;
        public static final int SELECT_TICKET_REQUEST = 1902;
        public static String EXTRA_PACKAGEVIEWMODEL = "packageviewmodel";
        public static String EXTRA_SEATLAYOUTVIEWMODEL = "seatlayoutviewmodel";
        public static String EXTRA_SEATSELECTEDMODEL = "selectedseatviewmodel";
        public static String EXTRA_VERIFY_RESPONSE = "verifyresponse";
        static String PROMOCODE = "promocode";
        static String PROMOCODE_DISCOUNT = "promocode_discount";
        static String PROMO_CASHBACK = "promocode_cashback";
        static String PROMO_FAILURE = "promocode_failure_message";
        static String PROMO_SUCCESS = "promocode_success_message";
        static String PROMO_STATUS = "promocode_status";
        public static String EVENT_OMS = "event_oms_android";
        public static final String LIKED_EVENTS = "liked_events";
        public static final String EVENTS_PREFS = "events_prefs";

    }

    public static UserSession getUserSession(Context context) {
        UserSession userSession = new UserSession(context);
        return userSession;
    }
}
