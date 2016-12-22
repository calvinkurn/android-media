package com.tokopedia.core.myproduct.model.editProductForm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.product.model.productdetail.ProductPreOrder;

import java.util.ArrayList;
import java.util.List;


public class EditProductForm {

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("config")
    @Expose
    private Object config;
    @SerializedName("server_process_time")
    @Expose
    private String serverProcessTime;

    /**
     *
     * @return
     * The data
     */
    public Data getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(Data data) {
        this.data = data;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The config
     */
    public Object getConfig() {
        return config;
    }

    /**
     *
     * @param config
     * The config
     */
    public void setConfig(Object config) {
        this.config = config;
    }

    /**
     *
     * @return
     * The serverProcessTime
     */
    public String getServerProcessTime() {
        return serverProcessTime;
    }

    /**
     *
     * @param serverProcessTime
     * The server_process_time
     */
    public void setServerProcessTime(String serverProcessTime) {
        this.serverProcessTime = serverProcessTime;
    }

    public static class Breadcrumb {

        @SerializedName("department_name")
        @Expose
        private String departmentName;
        @SerializedName("department_id")
        @Expose
        private String departmentId;
        @SerializedName("department_identifier")
        @Expose
        private String departmentIdentifier;
        @SerializedName("department_dir_view")
        @Expose
        private Integer departmentDirView;
        @SerializedName("department_tree")
        @Expose
        private Integer departmentTree;

        /**
         *
         * @return
         * The departmentName
         */
        public String getDepartmentName() {
            return departmentName;
        }

        /**
         *
         * @param departmentName
         * The department_name
         */
        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        /**
         *
         * @return
         * The departmentId
         */
        public String getDepartmentId() {
            return departmentId;
        }

        /**
         *
         * @param departmentId
         * The department_id
         */
        public void setDepartmentId(String departmentId) {
            this.departmentId = departmentId;
        }

        /**
         *
         * @return
         * The departmentIdentifier
         */
        public String getDepartmentIdentifier() {
            return departmentIdentifier;
        }

        /**
         *
         * @param departmentIdentifier
         * The department_identifier
         */
        public void setDepartmentIdentifier(String departmentIdentifier) {
            this.departmentIdentifier = departmentIdentifier;
        }

        /**
         *
         * @return
         * The departmentDirView
         */
        public Integer getDepartmentDirView() {
            return departmentDirView;
        }

        /**
         *
         * @param departmentDirView
         * The department_dir_view
         */
        public void setDepartmentDirView(Integer departmentDirView) {
            this.departmentDirView = departmentDirView;
        }

        /**
         *
         * @return
         * The departmentTree
         */
        public Integer getDepartmentTree() {
            return departmentTree;
        }

        /**
         *
         * @param departmentTree
         * The department_tree
         */
        public void setDepartmentTree(Integer departmentTree) {
            this.departmentTree = departmentTree;
        }

    }

    public static class Condition {

        @SerializedName("condition_id")
        @Expose
        private Integer conditionId;
        @SerializedName("condition_name")
        @Expose
        private String conditionName;

        /**
         *
         * @return
         * The conditionId
         */
        public Integer getConditionId() {
            return conditionId;
        }

        /**
         *
         * @param conditionId
         * The condition_id
         */
        public void setConditionId(Integer conditionId) {
            this.conditionId = conditionId;
        }

        /**
         *
         * @return
         * The conditionName
         */
        public String getConditionName() {
            return conditionName;
        }

        /**
         *
         * @param conditionName
         * The condition_name
         */
        public void setConditionName(String conditionName) {
            this.conditionName = conditionName;
        }

    }

    public static class Data {

        @SerializedName("wholesale_price")
        @Expose
        private List<WholesalePrice> wholesalePrice = new ArrayList<WholesalePrice>();
        @SerializedName("shop_is_gold")
        @Expose
        private Integer shopIsGold;
        @SerializedName("product")
        @Expose
        private Product product;
        @SerializedName("breadcrumb")
        @Expose
        private List<Breadcrumb> breadcrumb = new ArrayList<Breadcrumb>();
        @SerializedName("product_images")
        @Expose
        private List<ProductImage> productImages = new ArrayList<ProductImage>();
        @SerializedName("condition")
        @Expose
        private List<Condition> condition = new ArrayList<Condition>();
        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("etalase")
        @Expose
        private List<Etalase> etalase = new ArrayList<Etalase>();
        @SerializedName("server_id")
        @Expose
        private String serverId;
        @SerializedName("info")
        @Expose
        private Info info;
        @SerializedName("catalog")
        @Expose
        private Catalog catalog;
        @SerializedName("preorder")
        @Expose
        private ProductPreOrder preorder;


        /**
         *
         * @return
         * The preorder
         */
        public ProductPreOrder getPreorder() {
            return preorder;
        }

        /**
         *
         * @param preorder
         * The preorder
         */
        public void setPreorder(ProductPreOrder preorder) {
            this.preorder = preorder;
        }
        /**
         *
         * @return
         * The catalog
         */
        public Catalog getCatalog() {
            return catalog;
        }

        /**
         *
         * @param catalog
         * The catalog
         */
        public void setCatalog(Catalog catalog) {
            this.catalog = catalog;
        }

        /**
         *
         * @return
         * The wholesalePrice
         */
        public List<WholesalePrice> getWholesalePrice() {
            return wholesalePrice;
        }

        /**
         *
         * @param wholesalePrice
         * The wholesale_price
         */
        public void setWholesalePrice(List<WholesalePrice> wholesalePrice) {
            this.wholesalePrice = wholesalePrice;
        }

        /**
         *
         * @return
         * The shopIsGold
         */
        public Integer getShopIsGold() {
            return shopIsGold;
        }

        /**
         *
         * @param shopIsGold
         * The shop_is_gold
         */
        public void setShopIsGold(Integer shopIsGold) {
            this.shopIsGold = shopIsGold;
        }

        /**
         *
         * @return
         * The product
         */
        public Product getProduct() {
            return product;
        }

        /**
         *
         * @param product
         * The product
         */
        public void setProduct(Product product) {
            this.product = product;
        }

        /**
         *
         * @return
         * The breadcrumb
         */
        public List<Breadcrumb> getBreadcrumb() {
            return breadcrumb;
        }

        /**
         *
         * @param breadcrumb
         * The breadcrumb
         */
        public void setBreadcrumb(List<Breadcrumb> breadcrumb) {
            this.breadcrumb = breadcrumb;
        }

        /**
         *
         * @return
         * The productImages
         */
        public List<ProductImage> getProductImages() {
            return productImages;
        }

        /**
         *
         * @param productImages
         * The product_images
         */
        public void setProductImages(List<ProductImage> productImages) {
            this.productImages = productImages;
        }

        /**
         *
         * @return
         * The condition
         */
        public List<Condition> getCondition() {
            return condition;
        }

        /**
         *
         * @param condition
         * The condition
         */
        public void setCondition(List<Condition> condition) {
            this.condition = condition;
        }

        /**
         *
         * @return
         * The token
         */
        public String getToken() {
            return token;
        }

        /**
         *
         * @param token
         * The token
         */
        public void setToken(String token) {
            this.token = token;
        }

        /**
         *
         * @return
         * The etalase
         */
        public List<Etalase> getEtalase() {
            return etalase;
        }

        /**
         *
         * @param etalase
         * The etalase
         */
        public void setEtalase(List<Etalase> etalase) {
            this.etalase = etalase;
        }

        /**
         *
         * @return
         * The serverId
         */
        public String getServerId() {
            return serverId;
        }

        /**
         *
         * @param serverId
         * The server_id
         */
        public void setServerId(String serverId) {
            this.serverId = serverId;
        }

        /**
         *
         * @return
         * The info
         */
        public Info getInfo() {
            return info;
        }

        /**
         *
         * @param info
         * The info
         */
        public void setInfo(Info info) {
            this.info = info;
        }

    }

    public static class Etalase {

        @SerializedName("etalase_total_product")
        @Expose
        private String etalaseTotalProduct;
        @SerializedName("etalase_id")
        @Expose
        private Integer etalaseId;
        @SerializedName("etalase_num_product")
        @Expose
        private Integer etalaseNumProduct;
        @SerializedName("etalase_name")
        @Expose
        private String etalaseName;

        /**
         *
         * @return
         * The etalaseTotalProduct
         */
        public String getEtalaseTotalProduct() {
            return etalaseTotalProduct;
        }

        /**
         *
         * @param etalaseTotalProduct
         * The etalase_total_product
         */
        public void setEtalaseTotalProduct(String etalaseTotalProduct) {
            this.etalaseTotalProduct = etalaseTotalProduct;
        }

        /**
         *
         * @return
         * The etalaseId
         */
        public Integer getEtalaseId() {
            return etalaseId;
        }

        /**
         *
         * @param etalaseId
         * The etalase_id
         */
        public void setEtalaseId(Integer etalaseId) {
            this.etalaseId = etalaseId;
        }

        /**
         *
         * @return
         * The etalaseNumProduct
         */
        public Integer getEtalaseNumProduct() {
            return etalaseNumProduct;
        }

        /**
         *
         * @param etalaseNumProduct
         * The etalase_num_product
         */
        public void setEtalaseNumProduct(Integer etalaseNumProduct) {
            this.etalaseNumProduct = etalaseNumProduct;
        }

        /**
         *
         * @return
         * The etalaseName
         */
        public String getEtalaseName() {
            return etalaseName;
        }

        /**
         *
         * @param etalaseName
         * The etalase_name
         */
        public void setEtalaseName(String etalaseName) {
            this.etalaseName = etalaseName;
        }

    }

    public static class Info {

        @SerializedName("product_returnable")
        @Expose
        private Integer productReturnable;
        @SerializedName("shop_has_terms")
        @Expose
        private Integer shopHasTerms;

        /**
         *
         * @return
         * The productReturnable
         */
        public Integer getProductReturnable() {
            return productReturnable;
        }

        /**
         *
         * @param productReturnable
         * The product_returnable
         */
        public void setProductReturnable(Integer productReturnable) {
            this.productReturnable = productReturnable;
        }

        /**
         *
         * @return
         * The shopHasTerms
         */
        public Integer getShopHasTerms() {
            return shopHasTerms;
        }

        /**
         *
         * @param shopHasTerms
         * The shop_has_terms
         */
        public void setShopHasTerms(Integer shopHasTerms) {
            this.shopHasTerms = shopHasTerms;
        }

    }

    public static class Product {
        @SerializedName("product_name_editable")
        @Expose
        private String productNameEditable;
        @SerializedName("product_min_order")
        @Expose
        private String productMinOrder;
        @SerializedName("product_department_tree")
        @Expose
        private String productDepartmentTree;
        @SerializedName("product_name")
        @Expose
        private String productName;
        @SerializedName("product_weight")
        @Expose
        private String productWeight;
        @SerializedName("product_department_id")
        @Expose
        private String productDepartmentId;
        @SerializedName("product_short_desc")
        @Expose
        private String productShortDesc;
        @SerializedName("product_etalase_id")
        @Expose
        private String productEtalaseId;
        @SerializedName("product_id")
        @Expose
        private String productId;
        @SerializedName("product_status")
        @Expose
        private String productStatus;
        @SerializedName("product_currency")
        @Expose
        private String productCurrency;
        @SerializedName("product_weight_unit")
        @Expose
        private String productWeightUnit;
        @SerializedName("product_must_insurance")
        @Expose
        private String productMustInsurance;
        @SerializedName("product_etalase")
        @Expose
        private String productEtalase;
        @SerializedName("product_condition")
        @Expose
        private String productCondition;
        @SerializedName("product_price")
        @Expose
        private String productPrice;
        @SerializedName("product_currency_id")
        @Expose
        private String productCurrencyId;
        @SerializedName("product_url")
        @Expose
        private String productUrl;
        @SerializedName("product_condition_name")
        @Expose
        private String productConditionName;
        @SerializedName("product_insurance")
        @Expose
        private String productInsurance;

        /**
         *
         * @return
         * The productInsurance
         */
        public String getProductInsurance() {
            return productInsurance;
        }

        /**
         *
         * @param productInsurance
         * The product_insurance
         */
        public void setProductInsurance(String productInsurance) {
            this.productInsurance = productInsurance;
        }
        /**
         *
         * @return
         * The productConditionName
         */
        public String getProductConditionName() {
            return productConditionName;
        }

        /**
         *
         * @param productConditionName
         * The product_condition_name
         */
        public void setProductConditionName(String productConditionName) {
            this.productConditionName = productConditionName;
        }

        public String getProductNameEditable() {
            return productNameEditable;
        }

        public void setProductNameEditable(String productNameEditable) {
            this.productNameEditable = productNameEditable;
        }

        /**
         *
         * @return
         * The productMinOrder
         */
        public String getProductMinOrder() {
            return productMinOrder;
        }

        /**
         *
         * @param productMinOrder
         * The product_min_order
         */
        public void setProductMinOrder(String productMinOrder) {
            this.productMinOrder = productMinOrder;
        }

        /**
         *
         * @return
         * The productDepartmentTree
         */
        public String getProductDepartmentTree() {
            return productDepartmentTree;
        }

        /**
         *
         * @param productDepartmentTree
         * The product_department_tree
         */
        public void setProductDepartmentTree(String productDepartmentTree) {
            this.productDepartmentTree = productDepartmentTree;
        }

        /**
         *
         * @return
         * The productName
         */
        public String getProductName() {
            return productName;
        }

        /**
         *
         * @param productName
         * The product_name
         */
        public void setProductName(String productName) {
            this.productName = productName;
        }

        /**
         *
         * @return
         * The productWeight
         */
        public String getProductWeight() {
            return productWeight;
        }

        /**
         *
         * @param productWeight
         * The product_weight
         */
        public void setProductWeight(String productWeight) {
            this.productWeight = productWeight;
        }

        /**
         *
         * @return
         * The productDepartmentId
         */
        public String getProductDepartmentId() {
            return productDepartmentId;
        }

        /**
         *
         * @param productDepartmentId
         * The product_department_id
         */
        public void setProductDepartmentId(String productDepartmentId) {
            this.productDepartmentId = productDepartmentId;
        }

        /**
         *
         * @return
         * The productShortDesc
         */
        public String getProductShortDesc() {
            return productShortDesc;
        }

        /**
         *
         * @param productShortDesc
         * The product_short_desc
         */
        public void setProductShortDesc(String productShortDesc) {
            this.productShortDesc = productShortDesc;
        }

        /**
         *
         * @return
         * The productEtalaseId
         */
        public String getProductEtalaseId() {
            return productEtalaseId;
        }

        /**
         *
         * @param productEtalaseId
         * The product_etalase_id
         */
        public void setProductEtalaseId(String productEtalaseId) {
            this.productEtalaseId = productEtalaseId;
        }

        /**
         *
         * @return
         * The productId
         */
        public String getProductId() {
            return productId;
        }

        /**
         *
         * @param productId
         * The product_id
         */
        public void setProductId(String productId) {
            this.productId = productId;
        }

        /**
         *
         * @return
         * The productStatus
         */
        public String getProductStatus() {
            return productStatus;
        }

        /**
         *
         * @param productStatus
         * The product_status
         */
        public void setProductStatus(String productStatus) {
            this.productStatus = productStatus;
        }

        /**
         *
         * @return
         * The productCurrency
         */
        public String getProductCurrency() {
            return productCurrency;
        }

        /**
         *
         * @param productCurrency
         * The product_currency
         */
        public void setProductCurrency(String productCurrency) {
            this.productCurrency = productCurrency;
        }

        /**
         *
         * @return
         * The productWeightUnit
         */
        public String getProductWeightUnit() {
            return productWeightUnit;
        }

        /**
         *
         * @param productWeightUnit
         * The product_weight_unit
         */
        public void setProductWeightUnit(String productWeightUnit) {
            this.productWeightUnit = productWeightUnit;
        }

        /**
         *
         * @return
         * The productMustInsurance
         */
        public String getProductMustInsurance() {
            return productMustInsurance;
        }

        /**
         *
         * @param productMustInsurance
         * The product_must_insurance
         */
        public void setProductMustInsurance(String productMustInsurance) {
            this.productMustInsurance = productMustInsurance;
        }

        /**
         *
         * @return
         * The productEtalase
         */
        public String getProductEtalase() {
            return productEtalase;
        }

        /**
         *
         * @param productEtalase
         * The product_etalase
         */
        public void setProductEtalase(String productEtalase) {
            this.productEtalase = productEtalase;
        }

        /**
         *
         * @return
         * The productCondition
         */
        public String getProductCondition() {
            return productCondition;
        }

        /**
         *
         * @param productCondition
         * The product_condition
         */
        public void setProductCondition(String productCondition) {
            this.productCondition = productCondition;
        }

        /**
         *
         * @return
         * The productPrice
         */
        public String getProductPrice() {
            return productPrice;
        }

        /**
         *
         * @param productPrice
         * The product_price
         */
        public void setProductPrice(String productPrice) {
            this.productPrice = productPrice;
        }

        /**
         *
         * @return
         * The productCurrencyId
         */
        public String getProductCurrencyId() {
            return productCurrencyId;
        }

        /**
         *
         * @param productCurrencyId
         * The product_currency_id
         */
        public void setProductCurrencyId(String productCurrencyId) {
            this.productCurrencyId = productCurrencyId;
        }

        /**
         *
         * @return
         * The productUrl
         */
        public String getProductUrl() {
            return productUrl;
        }

        /**
         *
         * @param productUrl
         * The product_url
         */
        public void setProductUrl(String productUrl) {
            this.productUrl = productUrl;
        }

    }

    public static class ProductImage {

        @SerializedName("image_primary")
        @Expose
        private Integer imagePrimary;
        @SerializedName("image_id")
        @Expose
        private Integer imageId;
        @SerializedName("image_status")
        @Expose
        private Integer imageStatus;
        @SerializedName("image_src")
        @Expose
        private String imageSrc;
        @SerializedName("image_description")
        @Expose
        private String imageDescription;
        @SerializedName("image_src_300")
        @Expose
        private String imageSrc300;

        /**
         *
         * @return
         * The imagePrimary
         */
        public Integer getImagePrimary() {
            return imagePrimary;
        }

        /**
         *
         * @param imagePrimary
         * The image_primary
         */
        public void setImagePrimary(Integer imagePrimary) {
            this.imagePrimary = imagePrimary;
        }

        /**
         *
         * @return
         * The imageId
         */
        public Integer getImageId() {
            return imageId;
        }

        /**
         *
         * @param imageId
         * The image_id
         */
        public void setImageId(Integer imageId) {
            this.imageId = imageId;
        }

        /**
         *
         * @return
         * The imageStatus
         */
        public Integer getImageStatus() {
            return imageStatus;
        }

        /**
         *
         * @param imageStatus
         * The image_status
         */
        public void setImageStatus(Integer imageStatus) {
            this.imageStatus = imageStatus;
        }

        /**
         *
         * @return
         * The imageSrc
         */
        public String getImageSrc() {
            return imageSrc;
        }

        /**
         *
         * @param imageSrc
         * The image_src
         */
        public void setImageSrc(String imageSrc) {
            this.imageSrc = imageSrc;
        }

        /**
         *
         * @return
         * The imageDescription
         */
        public String getImageDescription() {
            return imageDescription;
        }

        /**
         *
         * @param imageDescription
         * The image_description
         */
        public void setImageDescription(String imageDescription) {
            this.imageDescription = imageDescription;
        }

        /**
         *
         * @return
         * The imageSrc300
         */
        public String getImageSrc300() {
            return imageSrc300;
        }

        /**
         *
         * @param imageSrc300
         * The image_src_300
         */
        public void setImageSrc300(String imageSrc300) {
            this.imageSrc300 = imageSrc300;
        }

    }

    public class WholesalePrice {

        @SerializedName("wholesale_max")
        @Expose
        private String wholesaleMax;
        @SerializedName("wholesale_min")
        @Expose
        private String wholesaleMin;
        @SerializedName("wholesale_price")
        @Expose
        private String wholesalePrice;

        /**
         *
         * @return
         *     The wholesaleMax
         */
        public String getWholesaleMax() {
            return wholesaleMax;
        }

        /**
         *
         * @param wholesaleMax
         *     The wholesale_max
         */
        public void setWholesaleMax(String wholesaleMax) {
            this.wholesaleMax = wholesaleMax;
        }

        /**
         *
         * @return
         *     The wholesaleMin
         */
        public String getWholesaleMin() {
            return wholesaleMin;
        }

        /**
         *
         * @param wholesaleMin
         *     The wholesale_min
         */
        public void setWholesaleMin(String wholesaleMin) {
            this.wholesaleMin = wholesaleMin;
        }

        /**
         *
         * @return
         *     The wholesalePrice
         */
        public String getWholesalePrice() {
            return wholesalePrice;
        }

        /**
         *
         * @param wholesalePrice
         *     The wholesale_price
         */
        public void setWholesalePrice(String wholesalePrice) {
            this.wholesalePrice = wholesalePrice;
        }

    }

    public class Catalog {

        @SerializedName("catalog_name")
        @Expose
        private String catalogName;
        @SerializedName("catalog_id")
        @Expose
        private String catalogId;

        /**
         *
         * @return
         * The catalogName
         */
        public String getCatalogName() {
            return catalogName;
        }

        /**
         *
         * @param catalogName
         * The catalog_name
         */
        public void setCatalogName(String catalogName) {
            this.catalogName = catalogName;
        }

        /**
         *
         * @return
         * The catalogId
         */
        public String getCatalogId() {
            return catalogId;
        }

        /**
         *
         * @param catalogId
         * The catalog_id
         */
        public void setCatalogId(String catalogId) {
            this.catalogId = catalogId;
        }

    }

}