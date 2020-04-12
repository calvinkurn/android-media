package com.tokopedia.product.addedit.imagepicker.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.cachemanager.SaveInstanceCacheManager;
import com.tokopedia.dialog.DialogUnify;
import com.tokopedia.imagepicker.common.util.ImageUtils;
import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity;
import com.tokopedia.product.addedit.R;
import com.tokopedia.product.addedit.detail.presentation.activity.AddEditProductDetailActivity;
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel;
import com.tokopedia.product.addedit.imagepicker.di.DaggerImagePickerCatalogComponent;
import com.tokopedia.product.addedit.imagepicker.di.ImagePickerCatalogComponent;
import com.tokopedia.product.addedit.imagepicker.di.ImagePickerCatalogModule;
import com.tokopedia.product.addedit.imagepicker.view.viewmodel.ImagePickerEditPhotoViewModel;
import com.tokopedia.product.addedit.mapper.AddEditProductMapper;
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel;
import com.tokopedia.product.addedit.tracking.ProductAddEditImageTracking;
import com.tokopedia.product.addedit.tracking.ProductAddStepperTracking;
import com.tokopedia.product.addedit.tracking.ProductEditEditImageTracking;
import com.tokopedia.user.session.UserSession;
import java.util.ArrayList;
import javax.inject.Inject;
import kotlin.Unit;
import static com.tokopedia.product.addedit.common.constant.AddEditProductConstants.EXTRA_CACHE_MANAGER_ID;
import static com.tokopedia.product.addedit.detail.presentation.constant.AddEditProductDetailConstants.REQUEST_CODE_DETAIL;
import static com.tokopedia.product.addedit.imagepicker.view.activity.ImagePickerAddProductActivity.IS_DRAFT;
import static com.tokopedia.product.addedit.imagepicker.view.activity.ImagePickerAddProductActivity.IS_EDIT;
import static com.tokopedia.product.addedit.imagepicker.view.activity.ImagePickerAddProductActivity.PRODUCT_INPUT_MODEL;
import static com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.EXTRA_IS_DRAFTING_PRODUCT;
import static com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.EXTRA_IS_EDITING_PRODUCT;
import static com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.EXTRA_PRODUCT_INPUT_MODEL;

public class ImagePickerEditPhotoActivity extends ImageEditorActivity implements HasComponent<ImagePickerCatalogComponent> {

    UserSession userSession;
    private static boolean isEditProduct = false;
    private static boolean isDraftProduct = false;
    private static ProductInputModel productInputModel = new ProductInputModel();

    @Inject
    ImagePickerEditPhotoViewModel viewModel;

    @Override
    public ImagePickerCatalogComponent getComponent() {
        return DaggerImagePickerCatalogComponent
                .builder()
                .imagePickerCatalogModule(new ImagePickerCatalogModule())
                .baseAppComponent(((BaseMainApplication) getContext().getApplicationContext()).getBaseAppComponent())
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        isEditProduct = intent.getBooleanExtra(IS_EDIT, false);
        isDraftProduct = intent.getBooleanExtra(IS_DRAFT, false);
        productInputModel = intent.getParcelableExtra(PRODUCT_INPUT_MODEL);
        userSession = new UserSession(getContext());
    }

    @Override
    public void trackOpen() {
        if (!isEditProduct) {
            ProductAddEditImageTracking.INSTANCE.trackScreen();
        }
    }

    @Override
    public void trackContinue() {
        if (isEditProduct) {
            ProductEditEditImageTracking.INSTANCE.trackContinue(userSession.getShopId());
        } else {
            ProductAddEditImageTracking.INSTANCE.trackContinue(userSession.getShopId());
        }
    }

    @Override
    public void trackBack() {
        if (isEditProduct) {
            ProductEditEditImageTracking.INSTANCE.trackBack(userSession.getShopId());
        } else {
            ProductAddEditImageTracking.INSTANCE.trackBack(userSession.getShopId());
        }
    }

    @Override
    protected void onFinishEditingImage(ArrayList<String> imageUrlOrPathList) {
        Intent intent = getFinishIntent(imageUrlOrPathList);
        trackContinue();
        moveActivity(imageUrlOrPathList, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null && requestCode == REQUEST_CODE_DETAIL) {
            setResult(Activity.RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        DialogUnify dialog = new DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE);
        dialog.setTitle(R.string.label_title_on_dialog);
        dialog.setDescription(getString(R.string.label_description_on_dialog));
        dialog.setPrimaryCTAText(getString(R.string.label_cta_primary_button_on_dialog));
        dialog.setTitle(getString(R.string.label_title_on_dialog));
        dialog.setSecondaryCTAText(getString(R.string.label_cta_secondary_button_on_dialog));
        dialog.setSecondaryCTAClickListener(() -> {
            moveToManageProduct();
            saveProductDraft(false);
            ProductAddStepperTracking.INSTANCE.trackDraftYes(userSession.getShopId());
            return Unit.INSTANCE;
        });
        dialog.setPrimaryCTAClickListener(() -> {
            finish();
            trackBack();
            ProductAddStepperTracking.INSTANCE.trackDraftCancel(userSession.getShopId());
            return Unit.INSTANCE; });
        dialog.show();
    }

    private void moveToManageProduct() {
        Intent intent = RouteManager.getIntent(this, ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST);
        startActivity(intent);
        finish();
    }

    private void moveActivity(ArrayList<String> imageUrlOrPathList, Intent intentFinish) {
        if (isEditProduct || isDraftProduct) {
            ImageUtils.deleteCacheFolder(ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE);
            ImageUtils.deleteCacheFolder(ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE_CAMERA);
            setResult(Activity.RESULT_OK, intentFinish);
            finish();
        } else {
            DetailInputModel detailInputModel = new DetailInputModel();
            detailInputModel.setImageUrlOrPathList(imageUrlOrPathList);
            productInputModel.setDetailInputModel(detailInputModel);

            SaveInstanceCacheManager saveInstanceCacheManager = new SaveInstanceCacheManager(this, true);
            saveInstanceCacheManager.put(EXTRA_PRODUCT_INPUT_MODEL, productInputModel);
            saveInstanceCacheManager.put(EXTRA_IS_EDITING_PRODUCT, isEditProduct);
            saveInstanceCacheManager.put(EXTRA_IS_DRAFTING_PRODUCT, isDraftProduct);

            Intent moveIntent = new Intent(this, AddEditProductDetailActivity.class);
            moveIntent.putExtra(EXTRA_CACHE_MANAGER_ID, saveInstanceCacheManager.getId());
            startActivityForResult(moveIntent, REQUEST_CODE_DETAIL);
        }
    }

    private void saveProductDraft(Boolean isUploading) {
        DetailInputModel detailInputModel = new DetailInputModel();
        detailInputModel.setImageUrlOrPathList(extraImageUrls);
        productInputModel.setDetailInputModel(detailInputModel);

        viewModel.saveProductDraft(AddEditProductMapper.INSTANCE.mapProductInputModelDetailToDraft(productInputModel), productInputModel.getDraftId(), isUploading);
        Toast.makeText(this, R.string.label_succes_save_draft, Toast.LENGTH_LONG).show();
    }
}
