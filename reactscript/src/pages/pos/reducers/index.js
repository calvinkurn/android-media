import { combineReducers } from 'redux'
import { PENDING, FULFILLED, REJECTED } from 'redux-promise-middleware'
import {
  FETCH_PRODUCTS,
  FETCH_ETALASE,
  PULL_TO_REFRESH,
  ON_ETALASE_CHANGE,
  RESET_PRODUCT_LIST,
  ADD_TO_CART,
  REMOVE_FROM_CART,
  INCREMENT_QTY,
  DECREMENT_QTY,
  CLEAR_CART,
  FETCH_SEARCH_PRODUCT,
  CLEAR_SEARCH_RESULTS,
  ON_SEARCH_QUERY_TYPE,
  SET_SEARCH_TEXT,
  ON_SUBMIT_FETCH_SEARCH_PRODUCT,
  FETCH_CART_FROM_CACHE,
  FETCH_BANK_FUlFILLED,
  FETCH_EMI_FUlFILLED,
  MAKE_PAYMENT_FUlFILLED,
  FETCH_SHOP_NAME,
  FETCH_SHOP_ID,
  PAYMENT_CHECKOUT_TO_NATIVE,
  MAKE_PAYMENT_V2,
  RELOAD_STATE,
} from '../actions/index'
import { bankData, emiData } from '../components/bankData';
// import { icons } from '../components/icon/index'



const shop = (state = {
  shopName: '',
  shopId: 0,
}, action) => {
  switch (action.type) {
    case `${FETCH_SHOP_NAME}_${FULFILLED}`:
      return {
        shopName: action.payload
      }
    case `${FETCH_SHOP_ID}_${FULFILLED}`:
      return {
        ...state,
        shopId: action.payload
      }
    default:
      return state
  }
}


const products = (state = {
  items: [],
  pagination: {
    start: 0,
    rows: 25,
  },
  isFetching: false,
  refreshing: false,
  canLoadMore: false
}, action) => {
  switch (action.type) {
    case `${FETCH_PRODUCTS}_${PENDING}`:
      return {
        ...state,
        isFetching: true,
      }
    case `${FETCH_PRODUCTS}_${FULFILLED}`:
      const products = action.payload.data.list || []
      const items = [...state.items, ...products]
      const nextUrl = action.payload.data.paging.uri_next
      const pagination = {
        ...state.pagination,
        start: items.length
      }
      return {
        items,
        pagination,
        isFetching: false,
        refreshing: false,
        canLoadMore: nextUrl ? true : false,
      }
    case `${FETCH_PRODUCTS}_${REJECTED}`:
      return {
        ...state,
        isFetching: false,
        refreshing: false,
      }
    case PULL_TO_REFRESH:
      return {
        items: [],
        pagination: {
          start: 0,
          rows: 25,
        },
        isFetching: false,
        refreshing: true
      }
    case RESET_PRODUCT_LIST:
      return {
        ...state,
        items: [],
        pagination: {
          start: 0,
          rows: 25,
        },
      }
    case `${ON_SUBMIT_FETCH_SEARCH_PRODUCT}_${FULFILLED}`:
      console.log(ON_SUBMIT_FETCH_SEARCH_PRODUCT)
      return {
        ...state,
        items: action.payload.data.data.products
      }
    default:
      return state
  }
}

const etalase = (state = {
  items: [{
    id: '0',
    name: 'Semua Etalase',
    alias: 'semua_etalase'
  }],
  selected: '0'
}, action) => {
  switch (action.type) {
    case `${FETCH_ETALASE}_${PENDING}`:
      return state
    case `${FETCH_ETALASE}_${FULFILLED}`:
      const data = action.payload.data.list || []
      const etalases = data.map(e => ({
        id: e.etalaseId,
        name: e.etalaseName,
        alias: e.etalaseAlias,
      }))

      return {
        ...state,
        items: [{
          id: '0',
          name: 'Semua Etalase',
          alias: 'semua_etalase'
        }, ...etalases],
      }
    case `${FETCH_ETALASE}_${REJECTED}`:
      return state
    case ON_ETALASE_CHANGE:
      return {
        ...state,
        selected: action.payload,
      }
    default:
      return state
  }
}

const cart = (state = {
  items: [],
  totalPrice: 0,
  isFetching: false,
}, action) => {
  switch (action.type) {
    case `${FETCH_CART_FROM_CACHE}_${PENDING}`:
      return {
        ...state,
        isFetching: true,
      }
    case `${FETCH_CART_FROM_CACHE}_${FULFILLED}`:
      const cartItems = action.payload.list
      const getTotalPrice = () => {
        let total_price = 0

        action.payload.list.map(res => {
          let price_per_item = res.product.product_price_unformatted * res.quantity
          total_price = total_price + price_per_item
        })
        return total_price
      }

      return {
        items: cartItems || [],
        totalPrice: getTotalPrice(),
        isFetching: false,
      }

    // case ADD_TO_CART:
    //   return {
    //     items: [...state.items, action.payload.item],
    //     totalPrice: state.totalPrice + action.payload.item.price
    //   }
    case `${FETCH_CART_FROM_CACHE}_${REJECTED}`:
      return {
        ...state,
        isFetching: false,
      }
    case ADD_TO_CART:

      return {
        items: [...state.items, action.payload.item],
        totalPrice: state.totalPrice + action.payload.item.price
      }

    case `${REMOVE_FROM_CART}_${FULFILLED}`:
      console.log(action.payload)
    const ItemToBeRemoved = state.items.filter(i => i.product_id === action.payload.pid)

    return {
      items: state.items.filter(i => action.payload.pid !== i.product_id),
      totalPrice: state.totalPrice - (ItemToBeRemoved[0].product.product_price_unformatted * ItemToBeRemoved[0].quantity)
    }

    case `${INCREMENT_QTY}_${FULFILLED}`:
      const itemQtyToBeIncr = state.items.filter(i => i.product_id === action.payload.pid)
      return {
        ...state,
        items: state.items.map(b => {
          if (action.payload.pid === b.product_id) {
            return Object.assign({}, b, {
              quantity: action.payload.quantity
            })
          } else {
            return b
          }
        }),
        totalPrice: state.totalPrice + itemQtyToBeIncr[0].product.product_price_unformatted
      }

    case `${DECREMENT_QTY}_${FULFILLED}`:
    const itemQtyToBeDecr = state.items.filter(i => i.product_id === action.payload.pid)
    if (itemQtyToBeDecr[0].quantity === 1) {
      return state
    }

    return {
      items: state.items.map(b => {
        if (action.payload.pid === b.product_id) {
          return Object.assign({}, b, {
            quantity: action.payload.quantity
          })
        } else {
          return b
        }
      }),
      totalPrice: state.totalPrice - itemQtyToBeDecr[0].product.product_price_unformatted
    }

    case `${CLEAR_CART}_${FULFILLED}`:
      return {
        items: [],
        totalPrice: 0
      }
    default:
      return state
  }
}


// ============= Checkout ============= //
const checkout = (state = {
  isFetchingParams: false,
  showLoadingPage: false,
  data: [],
  status_msg: ''
}, action) => {
  switch (action.type) {
    case `${PAYMENT_CHECKOUT_TO_NATIVE}_${PENDING}`:
      return {
        ...state,
        isFetchingParamsCheckout: true,
        showLoadingPage: true,
        data: [],
        status_msg: 'PROCESSING'
      }
    case `${PAYMENT_CHECKOUT_TO_NATIVE}_${FULFILLED}`:
      return {
        ...state,
        isFetchingParamsCheckout: false,
        showLoadingPage: false,
        data: action.payload,
        status_msg: 'SUCCESS'
      }
    case `${PAYMENT_CHECKOUT_TO_NATIVE}_${REJECTED}`:
      return {
        ...state,
        isFetchingParamsCheckout: false,
        showLoadingPage: false,
        data: [],
        status_msg: 'FAILED'
      }
    
    default:
      return state
  }
}
// ============= Checkout ============= //


// ============= Payment V2 ============= //
const paymentV2 = (state = {
  processing_isFetchingParams: false,
  processing_showLoadingPage: false,
  processing_data: [],
  processing_status_msg: ''
}, action) => {
  switch (action.type) {
    case `${MAKE_PAYMENT_V2}_${PENDING}`:
      return {
        ...state,
        processing_isFetchingParams: true,
        processing_showLoadingPage: true,
        processing_data: [],
        processing_status_msg: 'PROCESSING'
      }
    case `${MAKE_PAYMENT_V2}_${FULFILLED}`:
      // console.log('full')
      // console.log(action.payload)
      return {
        ...state,
        processing_isFetchingParams: false,
        processing_showLoadingPage: false,
        processing_data: action.payload,
        processing_status_msg: 'SUCCESS'
      }
    case `${MAKE_PAYMENT_V2}_${REJECTED}`:
      return {
        ...state,
        processing_isFetchingParams: false,
        processing_showLoadingPage: false,
        processing_data: [],
        processing_status_msg: 'FAILED'
      }
    
    default:
      return state
  }
}
// ============= Payment V2 ============= //


const payment = (state = {
  items: [],
  emiList: [],
  ccNum: '',
  mon: '',
  year: '',
  cvv: '',
  cardType: ''
}, action) => {
  switch (action.type) {
    case `${FETCH_BANK_FUlFILLED}_${PENDING}`:
      return {
        ...state,
        isFetching: true,
      }
    case `${FETCH_BANK_FUlFILLED}_${REJECTED}`:
      return {
        ...state, 
        isFetching: false
      }
    case `${FETCH_BANK_FUlFILLED}_${FULFILLED}`:
      return {
        ...state,
        items: action.payload
      }
      break;
    case `${FETCH_EMI_FUlFILLED}_${PENDING}`:
      return {
        ...state,
        isFetching: true,
      }
    case `${FETCH_EMI_FUlFILLED}_${REJECTED}`:
      return {
        ...state, 
        isFetching: false
      }
    case `${FETCH_EMI_FUlFILLED}_${FULFILLED}`:
     // const data =  action.payload.data.data.list;
      return {
        ...state,
        emiList: action.payload.data.data.list
      }
      break;

    case `${MAKE_PAYMENT_FUlFILLED}_${FULFILLED}`:
      return {
        ...state
      }
      break;
    case `${MAKE_PAYMENT_FUlFILLED}_${PENDING}`:
      return {
        ...state,
        isFetching: true,
      }
    case `${MAKE_PAYMENT_FUlFILLED}_${REJECTED}`:
      return {
        ...state,
        isFetching: false
      }
    case 'BANK_SELECTED':
      const bank_id = action.payload
      const newData = {
        items: state.items.map(i => {
          i.isSelected = false;
          if (i.bank_id === bank_id) {
            i.isSelected = true;
          }
          return i
        })
      }
      return { ...state, ...newData };
      break;
    case 'EMI_SELECTED':
      const emiId = action.payload
      const newEmiData = {
        emiList: state.emiList.map(i => {
          i.isSelected = false;
          if (i.id === emiId) {
            i.isSelected = true;
          }
          return i
        })
      }
      return { ...state, ...newEmiData };
      break;
    case 'SELECT_PAYMENT_OPTIONS':
      switch (action.payload.option) {
        case 'mon':
          return { ...state, mon: action.payload.value }
        case 'year':
          return { ...state, year: action.payload.value }
        case 'ccNum':
          return { ...state, ccNum: action.payload.value }
        case 'cvv':
          return { ...state, cvv: action.payload.value }
      }
    default:
      return state
  }
}

const search = (state = {
  items: [],
  query: '',
  isFetching: false,
}, action) => {
  switch (action.type) {
    case ON_SEARCH_QUERY_TYPE:
      return {
        ...state,
        query: action.payload,
      }
    case `${FETCH_SEARCH_PRODUCT}_${PENDING}`:
      return {
        ...state,
        isFetching: true,
      }
    case `${FETCH_SEARCH_PRODUCT}_${FULFILLED}`:
      return {
        ...state,
        items: action.payload.data.data.products.map(p => ({
          id: p.id,
          text: p.name
        })),
        isFetching: false,
      }
    case `${FETCH_SEARCH_PRODUCT}_${REJECTED}`:
      return {
        ...state,
        isFetching: false,
      }
    case CLEAR_SEARCH_RESULTS:
      return {
        items: [],
        query: '',
      }
    case SET_SEARCH_TEXT:
      console.log(action.payload)
      return {
        ...state,
        query: action.payload
      }
    default:
      return state
  }
}

const paymentInvoice = (state = {
  items: [{
    id: 160551106,
    price: "Rp 20.998.000",
    name: 'Oh Man! Baby Pomade Nutri Green 45gr',
    qty: 2,
    imageUrl: 'https://ecs7.tokopedia.net/img/cache/200-square/product-1/2017/8/10/193938857/193938857_022ba5db-40b1-4ca2-b460-aed833272f5b_1000_1000.jpg',
  }, {
    id: 160533448,
    price: "Rp 13.699.000",
    name: 'Happy Urang Aring 55ml',
    qty: 1,
    imageUrl: 'https://ecs7.tokopedia.net/img/cache/200-square/product-1/2017/4/28/160533448/160533448_8ee45562-709b-4da1-8505-355282ac5459_1000_1000.jpg',
  }],
  totalPrice: "Rp 34.697.000"
}, action) => {
  return state;
}

const transactionHistory = (state = {
  items: []
}, action) => {

  switch (action.type) {
    case 'FETCH_TRANSACTION_HISTORY':
      const data = [{
        orderName: "OkeShop Carrefour Kasablanca",
        orderId: "IVR/20170609/XVII/VI/13461162",
        time: '13 Jul 2017, 12:12 WIB',
        totalPrice: "Rp 34.697.000",
        status: "Berhasil",
        isCompleted: false,
        products: [
          {
            id: 160551106,
            price: "Rp 20.998.000",
            name: 'Oh Man! Baby Pomade Nutri Green 45gr',
            qty: 2,
            imageUrl: 'https://ecs7.tokopedia.net/img/cache/200-square/product-1/2017/8/10/193938857/193938857_022ba5db-40b1-4ca2-b460-aed833272f5b_1000_1000.jpg',
          },
          {
            id: 160533448,
            price: "Rp 13.699.000",
            name: 'Happy Urang Aring 55ml',
            qty: 1,
            imageUrl: 'https://ecs7.tokopedia.net/img/cache/200-square/product-1/2017/4/28/160533448/160533448_8ee45562-709b-4da1-8505-355282ac5459_1000_1000.jpg',
          }
        ]
      },
      {
        orderName: "OkeShop Carrefour Kasablanca",
        orderId: "IVR/20170609/XVII/VI/13461163",
        time: '13 Jul 2017, 12:12 WIB',
        totalPrice: "Rp 34.697.000",
        status: "Berhasil",
        isCompleted: true,
        products: [
          {
            id: 160551106,
            price: "Rp 20.998.000",
            name: 'Oh Man! Baby Pomade Nutri Green 45grsss  ',
            qty: 2,
            imageUrl: 'https://ecs7.tokopedia.net/img/cache/200-square/product-1/2017/8/10/193938857/193938857_022ba5db-40b1-4ca2-b460-aed833272f5b_1000_1000.jpg',
          },
          {
            id: 160533448,
            price: "Rp 13.699.000",
            name: 'Happy Urang Aring 55ml',
            qty: 1,
            imageUrl: 'https://ecs7.tokopedia.net/img/cache/200-square/product-1/2017/4/28/160533448/160533448_8ee45562-709b-4da1-8505-355282ac5459_1000_1000.jpg',
          }
        ]
      }];
      return {
        ...state,
        items: data
      }
      break;
  }

  return state;
}


const appReducer = combineReducers({
  products,
  etalase,
  cart,
  payment,
  checkout,
  paymentV2,
  search,
  paymentInvoice,
  transactionHistory,
  shop
})

const rootReducer = (state, action) => {
  if (action.type === 'RELOAD_STATE') {
    state = undefined
  }

  return appReducer(state, action)
}

export default rootReducer