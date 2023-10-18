<!--left header table-->
| **Applink** | <br/><br/>```<br/>val CATEGORY_FILTER = "$INTERNAL_TOKOPEDIA_NOW/category-filter?warehouse_id={warehouse_id}"<br/>```<br/><br/> |  |
| --- | --- | --- |
| **Team** | FE : [Reza Gama Hidayat](https://tokopedia.atlassian.net/wiki/people/5def15952702bc0ec7e775c5?ref=confluence) [Yogie Susdyastama Putra](https://tokopedia.atlassian.net/wiki/people/5c6bf2e6f1a05835f933bf30?ref=confluence) BE : [Muhamad Huda](https://tokopedia.atlassian.net/wiki/people/5c131b12128c7106f576c8a4?ref=confluence) [Rendi Surya (Unlicensed)](https://tokopedia.atlassian.net/wiki/people/5e3a5e4be697e80e5b41814e?ref=confluence)  |  |
| **Figma** | [(Figma) UX/UI - TokoNOW!](https://www.figma.com/file/vxDHPPyinsXDoqckLdLscY/UX%2FUI---Tokopedia-NOW!-%5BSpillover---September%5D?node-id=883%3A48019)  |  |
| **GQL** | [(Confluence) [Tokonow] Category Tree API](/wiki/spaces/TokoNow/pages/1452802766/GQL+Category+Tree+API)  |  |

### **Module**

`tokopedianow`



---

### **Classes**

- `TokoNowCategoryFilterFragment`
- `TokoNowCategoryFilterBottomSheet`



---

### **Description**

![image](../../res/tokopedia_now_category_filter_bottom_sheet.png)

This bottom sheet is used for showing all available categories filter options in TokoNOW.

- Category list inside the bottom sheet can be expanded / collapsed.
- The category list is limited only up to L2 (Level 2) Category.



---

### **How to Use**

1. Call RouteManager.getIntent() with `ApplinkConstInternalTokopediaNow.CATEGORY_FILTER`.
2. Make sure to pass `warehouseId` into RouteManager, as it’s required to fetch the category list.
3. Full code snippet `RouteManager.getIntent(context, ApplinkConstInternalTokopediaNow.CATEGORY_FILTER, warehouseId)`

 4. Make sure also to pass an extra parcaleble object value as `SelectedSortFilter`

