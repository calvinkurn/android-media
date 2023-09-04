<!--left header table-->
| **Applink** | <br/><br/>```<br/>val CATEGORY_LIST = "$INTERNAL_TOKOMART/category-list?warehouse_id={warehouse_id}"<br/>```<br/><br/> |  |
| --- | --- | --- |
| **Team** | PM : [Deo Nathaniel](https://tokopedia.atlassian.net/wiki/people/5c6be6f577edd55f716a2258?ref=confluence) FE : [Reza Gama Hidayat](https://tokopedia.atlassian.net/wiki/people/5def15952702bc0ec7e775c5?ref=confluence) [Yogie Susdyastama Putra](https://tokopedia.atlassian.net/wiki/people/5c6bf2e6f1a05835f933bf30?ref=confluence) BE : [Muhamad Huda](https://tokopedia.atlassian.net/wiki/people/5c131b12128c7106f576c8a4?ref=confluence) [Rendi Surya (Unlicensed)](https://tokopedia.atlassian.net/wiki/people/5e3a5e4be697e80e5b41814e?ref=confluence) UI : [Randi Winarbisono (Unlicensed)](https://tokopedia.atlassian.net/wiki/people/5d11964df46aa30c271c7d40?ref=confluence)  |  |
| **Figma** | [(Figma) UX/UI - TokoNOW!](https://www.figma.com/file/ywlnYgKxnz7AYYtPDhTs9T/UX%2FUI---TokoNOW!-%5BPilot-Release---July%5D?node-id=3441%3A13051)  |  |
| **GQL** | [(Confluence) [Tokonow] Category Tree API](/wiki/spaces/TokoNow/pages/1452802766/GQL+Category+Tree+API)  |  |
| **PRD** | [(Docs) Delivering Essentials within 2hr - [search for : 'category section']](https://docs.google.com/document/d/1pWsEA_85v9vZWmfxXo5DC0xsb7QWDu_cTNjMMQpnFsk/edit#)  |  |

### **Module**

`tokopedianow`



---

### **Classes**

- `TokoNowCategoryListFragment`
- `TokoNowCategoryListBottomSheet`



---

### **Description**

![image](../../res/tokopedia_now_category_bottom_sheet.png)

This bottom sheet is used for showing all available categories in TokoNOW.

- Category list inside the bottom sheet can be expanded / collapsed.
- The category list is limited only up to L2 (Level 2) Category.
- If L1 doesn’t have any L2 category, when expanded it will only show `“Semua Produk $categoryName”`.



---

### **How to Use**

1. Call RouteManager.route() with `ApplinkConstInternalTokoMart.CATEGORY_LIST`.
2. Make sure to pass `warehouseId` into RouteManager, as it’s required to fetch the category list.
3. Full code snippet `RouteManager.route(context, ApplinkConstInternalTokopediaNow.CATEGORY_LIST, $warehouseId)`.

