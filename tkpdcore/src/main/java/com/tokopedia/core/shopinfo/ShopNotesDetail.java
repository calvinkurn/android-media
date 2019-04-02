package com.tokopedia.core.shopinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.manage.shop.notes.model.ShopNote;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.shopinfo.facades.GetShopNote;
import com.tokopedia.core.shopinfo.models.NoteModel;
import com.tokopedia.core.util.MethodChecker;

import rx.subscriptions.CompositeSubscription;

public class ShopNotesDetail extends TActivity {

    private CompositeSubscription compositeSubscription= new CompositeSubscription();

    private class ViewHolder{
        TextView title;
        TextView date;
        TextView content;
        View progress;
        View titleBar;
    }

    private ViewHolder holder;
    private GetShopNote facadeShopNote;
    private String shopId;
    private String noteId;
    private String shopDomain;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_SHOP_NOTE;
    }

    public static Intent createIntent(Context context, String shopId, String shopDomain, String noteId){
        Intent intent =  new Intent(context, ShopNotesDetail.class);
        intent.putExtra("shop_id", shopId);
        intent.putExtra("note_id", noteId);
        intent.putExtra("shop_domain", shopDomain);
        return intent;
    }

    public static Intent createIntent(Context context, ShopNote shopNote) {
        Intent intent =  new Intent(context, ShopNotesDetail.class);
        intent.putExtra("shop_id", shopNote.getShopId());
        intent.putExtra("note_id", shopNote.getNoteId());
        intent.putExtra("shop_domain", shopNote.getShopDomain());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_shop_notes_detail);
        compositeSubscription = RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        shopId = getIntent().getStringExtra("shop_id");
        noteId = getIntent().getStringExtra("note_id");
        shopDomain = getIntent().getStringExtra("shop_domain");
        initView();
        initFacade();
        facadeShopNote.setCompositeSubscription(compositeSubscription);
        facadeShopNote.getNoteDetail2(shopId, shopDomain, noteId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView(){
        holder = new ViewHolder();
        holder.title = (TextView)findViewById(R.id.title);
        holder.date = (TextView)findViewById(R.id.date);
        holder.content = (TextView)findViewById(R.id.content);
        holder.progress = findViewById(R.id.progress);
        holder.titleBar = findViewById(R.id.note_layout);
    }

    private void initFacade(){
        facadeShopNote = new GetShopNote(this);
        facadeShopNote.setOnGetNoteDetailListener(onGetNoteDetail());
    }

    private GetShopNote.OnGetNoteDetailListener onGetNoteDetail(){
        return new GetShopNote.OnGetNoteDetailListener() {
            @Override
            public void onSuccess(NoteModel model) {
                showNote(model);
            }

            @Override
            public void onFailure() {

            }
        };
    }

    private void showNote(NoteModel model){
        holder.progress.setVisibility(View.GONE);
        holder.content.setVisibility(View.VISIBLE);
        holder.titleBar.setVisibility(View.VISIBLE);
        holder.title.setText(MethodChecker.fromHtml(model.title));
        holder.date.setText(model.update);
        holder.content.setText(MethodChecker.fromHtml(model.content.replace("\n", "<br>").replace("\n","<br/>")));
    }
}
