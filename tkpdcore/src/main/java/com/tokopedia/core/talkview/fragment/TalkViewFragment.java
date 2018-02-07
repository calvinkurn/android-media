package com.tokopedia.core.talkview.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.talk.talkproduct.fragment.TalkProductFragment;
import com.tokopedia.core.talkview.adapter.TalkViewAdapter;
import com.tokopedia.core.talkview.inbox.model.TalkDetail;
import com.tokopedia.core.talkview.listener.TalkViewListener;
import com.tokopedia.core.talkview.method.DeleteTalkDialog;
import com.tokopedia.core.talkview.method.FollowTalkDialog;
import com.tokopedia.core.talkview.method.ReportTalkDialog;
import com.tokopedia.core.talkview.model.TalkBaseModel;
import com.tokopedia.core.talkview.presenter.TalkViewPresenter;
import com.tokopedia.core.talkview.presenter.TalkViewPresenterImpl;
import com.tokopedia.core.talkview.product.model.CommentTalk;
import com.tokopedia.core.util.LabelUtils;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.TokenHandler;
import com.tokopedia.core.util.ToolTipUtils;
import com.tokopedia.core.util.ValidationTextUtil;
import com.tokopedia.core.util.getproducturlutil.GetProductUrlUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by stevenfredian on 5/10/16.
 */
public abstract class TalkViewFragment extends BasePresenterFragment<TalkViewPresenter>
        implements TalkViewListener {

    String from;
    int position;
    Parcelable talk;

    protected String talkID;
    protected String productName;
    protected String prodImgUri;
    protected String userName;
    protected Spanned message;
    protected String createTime;
    protected String userImgUri;

    protected String productID;
    protected String userIDTalk;
    protected String shopID;
    protected String userGender;
    protected String headUserLabel;
    protected int isOwner;
    protected int isFollow;
    protected int totalComment;
    protected int readStatus;
    protected String reputationHeader;
    protected String positiveHeader;
    protected String negativeHeader;
    protected String neutralHeader;
    protected int noReputationHeader;
    int headerHeight;

    private boolean isRequest;
    protected String content;
    private PagingHandler paging;
    protected ArrayList<TalkBaseModel> items;
    private TalkViewAdapter adapter;
    private LinearLayoutManager layoutManager;
    LabelUtils label;
    TokenHandler token;

    TalkBaseModel LOADING;
    TalkBaseModel RETRY;

    int paramMaster=0;

    @BindView(R2.id.content_lv) LinearLayout contentLv;
    @BindView(R2.id.new_comment) EditText comment;
    @BindView(R2.id.send_but) ImageView sendBut;
    @BindView(R2.id.comment_list) RecyclerView recyclerView;
    @BindView(R2.id.prod_img) ImageView pImageView;
    @BindView(R2.id.prod_name) TextView pNameView;
    @BindView(R2.id.add_comment_area) View addCommentArea;
    @BindView(R2.id.add_url) View addUrlView;
    @BindView(R2.id.user_ava) ImageView userTalkImageView;
    @BindView(R2.id.user_name) TextView userView;
    @BindView(R2.id.create_time) TextView timeView;
    @BindView(R2.id.message) TextView messageView;
    @BindView(R2.id.rank) TextView rank;
    @BindView(R2.id.but_overflow) View buttonOverflow;
    @BindView(R2.id.reputation) LinearLayout reputation;
    @BindView(R2.id.reputation_user) View reputationUser;
    @BindView(R2.id.rep_icon) ImageView iconReputation;
    @BindView(R2.id.rep_rating) TextView textReputation;
    @BindView(R2.id.swipe_refresh_layout) SwipeRefreshLayout swipe;
    @BindView(R2.id.header) View header;
    @BindView(R2.id.main) View mainLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOADING = TalkBaseModel.create(TalkBaseModel.LOADING);
        RETRY = TalkBaseModel.create(TalkBaseModel.RETRY);
        items = new ArrayList<>();
        adapter = getAdapter();
        adapter.setOnRetryListenerRV(onRetryClicked());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.talk_view, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_refresh) {
            if (!isRequest) {
                swipe.setRefreshing(true);
                refresh();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {
        displayLoading(true);
        getComment();
    }

    @Override
    public void onSaveState(Bundle state) {
        presenter.saveState(state, items, layoutManager.findLastCompletelyVisibleItemPosition()
                , paging.getPage(), paging.CheckNextPage());
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        presenter.restoreState(savedState);
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    protected void initialPresenter() {
        presenter = new TalkViewPresenterImpl(this, from);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        from = arguments.getString("from");
        talk = arguments.getParcelable("talk");
        talkID = arguments.getString("talk_id", "");
        shopID = arguments.getString("shop_id", "");
        getFromBundle(talk);
        position = arguments.getInt("position");
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_talk_view;
    }

    @Override
    protected void initView(View view) {
        layoutManager = new LinearLayoutManager(view.getContext());
        swipe.setEnabled(false);
        swipe.setColorSchemeResources(R.color.tkpd_dark_green);
        recyclerView.setLayoutManager(layoutManager);
        label = LabelUtils.getInstance(context, userView);
        parseHeader();
        if (!SessionHandler.isV4Login(getActivity())) {
            addCommentArea.setVisibility(View.GONE);
        }
        if (SessionHandler.isV4Login(context)) {
            buttonOverflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopup(v);
                }
            });
        } else {
            buttonOverflow.setVisibility(View.GONE);
        }

        paging = new PagingHandler();

        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(checkKeyboardAppearance());

        if(checkHasNoShop()) addUrlView.setVisibility(View.GONE);
    }


    private boolean checkHasNoShop(){
        return !SessionHandler.isUserHasShop(getActivity());
    }

    private ViewTreeObserver.OnGlobalLayoutListener checkKeyboardAppearance() {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(mainLayout == null){
                    int heightDiff = mainLayout.getRootView().getHeight() - mainLayout.getHeight();
                    if (keyboardAppears(heightDiff) && adapter.getItemCount()>0) { // if more than 200 dp, it's probably a keyboard...
                        header.setVisibility(View.GONE);
                    }else{
                        header.setVisibility(View.VISIBLE);
                    }
                }
            }
        };
    }

    private boolean keyboardAppears(int heightDiff) {
        return heightDiff > dpToPx(getActivity(), 200);
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    @Override
    protected void setViewListener() {
        pNameView.setOnClickListener(goToProduct());
        addUrlView.setOnClickListener(addUrl());
        sendBut.setOnClickListener(send());
        reputationUser.setOnClickListener(showToolTip());
        userView.setOnClickListener(goToPeople());
        recyclerView.addOnScrollListener(OnScrollRecyclerView());
    }


    @Override
    protected void initialVar() {
        token = new TokenHandler();
        recyclerView.setAdapter(adapter);
        headerHeight = header.getHeight();
    }

    protected abstract TalkViewAdapter getAdapter();

    @Override
    protected void setActionVar() {

    }

    protected void parseHeader() {
        if (talk != null){

            label.giveLabel(headUserLabel);
            pNameView.setText(productName);
            ImageHandler.loadImageRounded2(context, pImageView, prodImgUri);
            ImageHandler.loadImageCircle2(context, userTalkImageView, userImgUri);
            timeView.setText(createTime);
            messageView.setText(message);
            userView.setText(userName);
            reputation.setVisibility(View.GONE);
            reputationUser.setVisibility(View.VISIBLE);
            textReputation.setText(String.format("%s%%", reputationHeader));
            if (noReputationHeader == 0) {
                iconReputation.setImageResource(R.drawable.ic_icon_repsis_smile_active);
                textReputation.setVisibility(View.VISIBLE);
            } else {
                iconReputation.setImageResource(R.drawable.ic_icon_repsis_smile);
                textReputation.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onStateResponse(List<TalkBaseModel> list, int position, int page, boolean hasNext) {
        items.clear();
        items.addAll(list);
        adapter.notifyDataSetChanged();
        setScrollPosition(position);
        paging.setPage(page);
        paging.setHasNext(hasNext);
    }


    @Override
    public void successGet(JSONObject result) {
        isRequest = false;
        displayLoading(false);
        swipe.setRefreshing(false);
        parseResult(result);
        adapter.notifyDataSetChanged();
        getActivity().setResult(Activity.RESULT_OK, getResult());
    }

    private void parseResult(JSONObject result) {
        if (paging.getPage() == 1) items.clear();
        int size = getSize(result);
        getResultType(result);
        setLoadMore(size == 20, size);
        if (size==20 && paging.getPage() <= 2)setScrollPosition(size);
        else setScrollPosition(size-1);
    }

    protected abstract void getResultType(JSONObject result);

    protected abstract int getSize(JSONObject object);

    private void setScrollPosition(int position) {
        layoutManager.scrollToPosition(position);
    }

    private void setLoadMore(boolean status, int size) {
        if (status) {
            paging.setHasNext(true);
            paging.nextPage();
            displayLoading(true);
        } else {
            paging.setHasNext(false);
        }
    }

    @Override
    public void showError(String error) {
        isRequest = false;
        swipe.setRefreshing(false);
        displayRetry(true);
        SnackbarManager.make(getActivity(), error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void successReply(String string) {
        UnifyTracking.eventDiscussionSendSuccess(from);
        SnackbarManager.make(getActivity(), string
                , Snackbar.LENGTH_SHORT).show();
        sendBut.setEnabled(true);
        totalComment++;
        refreshFromDB();
    }

    private void refreshFromDB() {
        setParamMaster();
        refresh();
        resetParamMaster();
    }

    private void resetParamMaster() {
        paramMaster = 0;
    }

    private void setParamMaster() {
        paramMaster = 1;
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
        paging.resetPage();
        getComment();
    }

    @Override
    public void errorReply(String error) {
        UnifyTracking.eventDiscussionSendError(from);
        revertTalk();
        comment.setText(content);
        SnackbarManager.make(getActivity(), error, Snackbar.LENGTH_LONG).show();
        sendBut.setEnabled(true);
    }

    private void revertTalk() {
        if(items.size() > 0) {
            items.remove(items.size() - 1);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public String getTalkID() {
        return talkID;
    }

    @Override
    public String getProductID() {
        return productID;
    }

    @Override
    public String getCommentContent() {
        return content;
    }

    @Override
    public String getShopID() {
        return shopID;
    }

    @Override
    public String getTextMessage() {
        return message.toString();
    }

    protected abstract void getFromBundle(Parcelable bundle);

    private void getComment() {
        isRequest = true;
        presenter.getComment(getActivity(), getParamComment());
    }

    private View.OnClickListener send() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment.setError(null);
                if (ValidationTextUtil.isValidText(5, comment.getText().toString().trim())) {
                    if (isRequest) {
                        SnackbarManager.make(getActivity(), "Mohon tunggu sesaat lagi", Snackbar.LENGTH_LONG);
                    } else {
                        KeyboardHandler.DropKeyboard(context, sendBut);
                        content = comment.getText().toString();
                        comment.setText("");
                        comment.clearFocus();
                        addDummy();
                        adapter.notifyDataSetChanged();
                        setScrollPosition(items.size() - 1);
                        presenter.reply(context, getParam());
                        sendBut.setEnabled(false);
                    }
                } else {
                    int length = comment.getText().toString().trim().length();
                    if (length == 0) {
                        comment.setError(getString(R.string.error_field_required));
                    }else if (length < 5 || length > 0) {
                        comment.setError(getString(R.string.error_min_5_character));
                    }
                }
            }
        };
    }

    protected abstract void addDummy();


    private View.OnClickListener addUrl() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetProductUrlUtil getUrl = GetProductUrlUtil.createInstance(getActivity());
                getUrl.getOwnShopProductUrl(new GetProductUrlUtil.OnGetUrlInterface() {
                    @Override
                    public void onGetUrl(String url) {
                        comment.setText(String.format("%s%s", comment.getText(), url));
                        comment.setSelection(comment.length());
                        comment.setError(null);
                    }
                });
            }
        };
    }

    private View.OnClickListener goToProduct() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ProductDetailRouter
                        .createInstanceProductDetailInfoActivity(getActivity(), productID);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener goToPeople() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(
                        PeopleInfoNoDrawerActivity.createInstance(context, userIDTalk)
                );
            }
        };
    }

    private View.OnClickListener showToolTip() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolTipUtils.showToolTip(showHeaderToolTip(), v);
            }
        };
    }

    private View showHeaderToolTip() {
        return ToolTipUtils.setToolTip(context, R.layout.view_tooltip_user, new ToolTipUtils.ToolTipListener() {
            @Override
            public void setView(View view) {
                TextView smile = (TextView) view.findViewById(R.id.text_smile);
                TextView netral = (TextView) view.findViewById(R.id.text_netral);
                TextView bad = (TextView) view.findViewById(R.id.text_bad);
                smile.setText(positiveHeader);
                netral.setText(neutralHeader);
                bad.setText(negativeHeader);
            }

            @Override
            public void setListener() {

            }
        });
    }

    public Map<String, String> getParam() {
        Map<String, String> param = new HashMap<>();
        param.put("product_id", productID);
        param.put("talk_id", talkID);
        param.put("text_comment", content);
        return param;
    }

    public Map<String, String> getParamComment() {
        Map<String, String> param = new HashMap<>();
        param.put("page", String.valueOf(paging.getPage()));
        param.put("shop_id", shopID);
        param.put("talk_id", talkID);
        if(paramMaster==1) param.put("master", String.valueOf(paramMaster));
        return param;
    }

    public void showPopup(final View v) {
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        if (getMenuID() != 0)
            inflater.inflate(getMenuID(), popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DialogFragment dialog;
                if (item.getItemId() == R.id.action_delete || item.getItemId() == R.id.action_delete_talk) {
                    dialog = DeleteTalkDialog.createInstance(deleteListener());
                    dialog.show(getFragmentManager(), DeleteTalkDialog.FRAGMENT_TAG);
                    return true;
                } else if (item.getItemId() == R.id.action_follow || item.getItemId() == R.id.action_unfollow) {
                    dialog = FollowTalkDialog.createInstance(followListener(), isFollow);
                    dialog.show(getFragmentManager(), FollowTalkDialog.FRAGMENT_TAG);
                    return true;
                } else if (item.getItemId() == R.id.action_report) {
                    dialog = ReportTalkDialog.createInstance(reportListener());
                    dialog.show(getFragmentManager(), ReportTalkDialog.FRAGMENT_TAG);
                    return true;
                } else {
                    return false;
                }
            }
        });
        popup.show();
    }

    private ReportTalkDialog.ReportTalkListener reportListener() {
        return new ReportTalkDialog.ReportTalkListener() {
            @Override
            public void reportTalk(String s) {
                presenter.reportTalk();
            }
        };
    }

    private DeleteTalkDialog.DeleteTalkListener deleteListener() {
        return new DeleteTalkDialog.DeleteTalkListener() {
            @Override
            public void deleteTalk() {
                swipe.setRefreshing(true);
                presenter.deleteTalk();
            }
        };
    }

    private FollowTalkDialog.FollowTalkListener followListener() {
        return new FollowTalkDialog.FollowTalkListener() {
            @Override
            public void followTalk() {
                presenter.followTalk();
            }
        };
    }

    private int getMenuID() {
        int menuID;
//        if (token.getLoginID(context).equals(userIDTalk)) {
//            if (isFollow==1) {
//                menuID = R.menu.unfollow_delete_menu;
//            } else {
//                menuID = R.menu.follow_delete_menu;
//            }
//        } else {
//            menuID = R.menu.report_menu;
//        }
        if (shopID.equals(SessionHandler.getShopID(context))) {
            menuID = R.menu.report_menu;
        } else {
            if (token.getLoginID(context).equals(userIDTalk)) {
                if (isFollow == 1) {
                    menuID = R.menu.unfollow_delete_menu;
                } else {
                    menuID = R.menu.follow_delete_menu;
                }
            } else {
                if (isFollow == 1) {
                    menuID = R.menu.unfollow_report_menu;
                } else {
                    menuID = R.menu.follow_report_menu;
                }
            }
        }
        return menuID;
    }


    private TalkViewAdapter.OnRetryListener onRetryClicked() {
        return new TalkViewAdapter.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                displayRetry(false);
                getComment();
                adapter.notifyDataSetChanged();
            }
        };
    }

    private RecyclerView.OnScrollListener OnScrollRecyclerView() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isFirstPosition() && !isRequest) {
                    if (paging.CheckNextPage()) {
                        getComment();
                    }
                }
            }
        };
    }

    private boolean isFirstPosition() {
        return layoutManager.findFirstVisibleItemPosition() == 0;
    }

    private void displayLoading(boolean status) {
        Log.d("tev loading", String.valueOf(status));
        Log.d("tev loading bef", String.valueOf(items.size()));
        if (status) {
            items.add(0, LOADING);
        } else {
            if(items.size()>0 && items.get(0).getType()==TalkBaseModel.LOADING){
                items.remove(0);
            }
        }
        Log.d("tev loading aft", String.valueOf(items.size()));
        adapter.notifyDataSetChanged();
    }

    private void displayRetry(boolean status) {
        if (items.size() == 0) items.add(0, RETRY);
        if (status) {
            items.set(0, RETRY);
        } else {
            items.set(0, LOADING);
        }
        Log.d("tev retry aft", String.valueOf(items.size()));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        presenter.unSubscribe();
        super.onDestroy();
    }

    public Intent getResult() {
        Intent data = getActivity().getIntent();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putInt("total_comment", totalComment);
        bundle.putInt("is_follow", isFollow);
        bundle.putInt("read_status", 0);
        data.putExtras(bundle);
        return data;
    }

    public void successDelete(String string) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        getActivity().setResult(TalkProductFragment.RESULT_DELETE, new Intent().putExtras(bundle));
        getActivity().finish();
    }

    public void errorDelete(String error) {
        swipe.setRefreshing(false);
        SnackbarManager.make(getActivity(), error, Snackbar.LENGTH_LONG).show();
    }

    public void successFollow(String string) {
        isFollow ^= 1;
        String[] array = getResources().getStringArray(R.array.message_follow);
        SnackbarManager.make(getActivity(), array[isFollow], Snackbar.LENGTH_LONG).show();
        getActivity().setResult(Activity.RESULT_OK, getResult());
    }

    public void errorFollow(String string) {
        SnackbarManager.make(getActivity(),
                context.getResources().getString(R.string.msg_connection_timeout),
                Snackbar.LENGTH_LONG).show();
    }

    public void successReport(String string) {
        SnackbarManager.make((Activity)context,
                context.getString(R.string.toast_success_report),
                Snackbar.LENGTH_LONG).show();
    }

    public void errorReport(String error) {
        SnackbarManager.make(getActivity(), error, Snackbar.LENGTH_LONG).show();
    }

    public void setSwipe(boolean status) {
        swipe.setRefreshing(status);
    }


    public void successDeleteComment(String string, int position) {
        totalComment--;
        items.remove(position);
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(false);
        SnackbarManager.make(getActivity(),
                context.getResources().getString(R.string.message_success_delete_comment_talk),
                Snackbar.LENGTH_LONG).show();
        getActivity().setResult(Activity.RESULT_OK, getResult());
    }

    public void errorDeleteComment(String error) {
        swipe.setRefreshing(false);
        SnackbarManager.make(getActivity(), error, Snackbar.LENGTH_LONG).show();
    }

    public void successReportComment(String string, int position) {
        SnackbarManager.make((Activity)context,
                context.getString(R.string.toast_success_report),
                Snackbar.LENGTH_LONG).show();
    }

    public void errorReportComment(String error) {
        SnackbarManager.make(getActivity(), error, Snackbar.LENGTH_LONG).show();
    }

    public void deleteCommentTalk(CommentTalk talk, int position) {
        presenter.deleteCommentTalk(talk, position);
    }

    public void deleteCommentTalk(TalkDetail talk, int position) {
        presenter.deleteCommentTalk(talk, position);
    }

    public void reportCommentTalk(CommentTalk talk, int position) {
        presenter.reportCommentTalk(talk, position);
    }

    public void reportCommentTalk(TalkDetail talk, int position) {
        presenter.reportCommentTalk(talk, position);
    }
}
