![image](../../res/tokopedia_now_error_state.png)

<!--left header table-->
| **Type Factory** | `TokoNowServerErrorTypeFactory` |
| --- | --- |
| **View Holder** | `TokoNowServerErrorViewHolder` |
| **UI Model** | `TokoNowServerErrorUiModel` |
| **Listener** | `ServerErrorListener` |
| **FE** | [Reza Gama Hidayat](https://tokopedia.atlassian.net/wiki/people/5def15952702bc0ec7e775c5?ref=confluence) [Yogie Susdyastama Putra](https://tokopedia.atlassian.net/wiki/people/5c6bf2e6f1a05835f933bf30?ref=confluence) [Said Faisal](https://tokopedia.atlassian.net/wiki/people/5e25eee0ee264b0e745862c3?ref=confluence)  |

**How to Use**

1. Add `ServerErrorListener` implementation into fragment.
2. Add `TokoNowServerErrorTypeFactory` implementation into adapter type factory & override type `TokoNowServerErrorUiModel`.
3. Add `TokoNowServerErrorViewHolder` into `createViewHolder` method.
4. Add `TokoNowServerErrorUiModel` item into adapter.

**Example**



```
class MyTypeFactory(
  private val serverErrorListener: ServerErrorListener
): TokoNowServerErrorTypeFactory {
    ...
    override fun type(uiModel: TokoNowServerErrorUiModel) = TokoNowServerErrorViewHolder.LAYOUT
    ...
    
    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ...
            TokoNowServerErrorViewHolder.LAYOUT -> TokoNowServerErrorViewHolder(view, serverErrorListener)
            ...
            else -> super.createViewHolder(view, type)
        }
    }
}

class Fragment(): ServerErrorListener {
    override fun onClickRetryButton() {
        // refetch all data from gql
    }
}
```

