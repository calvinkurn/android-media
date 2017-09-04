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
  CLEAR_CART
} from '../actions/index'
import {bankData, emiData} from '../components/bankData';

const products = (state = {
  items: [],
  pagination: {
    start: 0,
    rows: 25,
  },
  isFetching: false,
  refreshing: false,
  canLoadMore: false,
}, action) => {
  switch (action.type) {
    case `${FETCH_PRODUCTS}_${PENDING}`:
      return {
        ...state,
        isFetching: true,
      }
    case `${FETCH_PRODUCTS}_${FULFILLED}`:
      const products = action.payload.data.data.products || []
      const items = [...state.items, ...products]
      const nextUrl = action.payload.data.data.paging.uri_next
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
    default:
      return state
  }
}

const etalase = (state = {
  items: [{
    id: '0',
    name: 'All Products',
    alias: 'all_products'
  }],
  selected: '0'
}, action) => {
  switch (action.type) {
    case `${FETCH_ETALASE}_${PENDING}`:
      return state
    case `${FETCH_ETALASE}_${FULFILLED}`:
      const etalases = action.payload.data.data.map(e => ({
        id: e.menu_id,
        name: e.menu_name,
        alias: e.menu_alias,
      }))

      return {
        ...state,
        items: [{
          id: '0',
          name: 'All Products',
          alias: 'all_products'
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
  items: [{
    id: 160551106,
    price: 40,
    name: 'LEBIH HEMAT - Lovana Bodylotion Milky Cupcake 250ml',
    qty: 1,
    imageUrl: 'https://ecs7.tokopedia.net/img/cache/200-square/product-1/2017/3/17/17437275/17437275_1878b3d2-ce9c-4ab3-b345-64da204ec935.jpg',
  }, {
    id: 160533448,
    price: 40,
    name: 'Happy Urang Aring 55ml',
    qty: 1,
    imageUrl: 'https://ecs7.tokopedia.net/img/cache/200-square/product-1/2017/4/28/160533448/160533448_8ee45562-709b-4da1-8505-355282ac5459_1000_1000.jpg',
  }, {
    id: 193938857,
    price: 40,
    name: 'Oh Man! Baby Pomade Nutri Green 45gr',
    qty: 1,
    imageUrl: 'https://ecs7.tokopedia.net/img/cache/200-square/product-1/2017/8/10/193938857/193938857_022ba5db-40b1-4ca2-b460-aed833272f5b_1000_1000.jpg',
  },],
  totalPrice: 120
}, action) => {
  switch (action.type) {
    case ADD_TO_CART:

      return {
        items: [...state.items, action.payload.item],
        totalPrice: state.totalPrice + action.payload.item.price
      }

    case REMOVE_FROM_CART:
      const ItemToBeRemoved = state.items.filter(i => i.id === action.payload.id)

      return {
        items: state.items.filter(i => action.payload.id !== i.id),
        totalPrice: state.totalPrice - (ItemToBeRemoved[0].price * ItemToBeRemoved[0].qty)
      }

    case INCREMENT_QTY:
      const itemQtyToBeIncr = state.items.filter(i => i.id === action.payload.id)
      return {
        items: state.items.map(b => {
          if (action.payload.id === b.id) {
            return Object.assign({}, b, {
              qty: b.qty + 1
            })
          } else {
            return b
          }
        }),
        totalPrice: state.totalPrice + itemQtyToBeIncr[0].price
      }

    case DECREMENT_QTY:
      const itemQtyToBeDecr = state.items.filter(i => i.id === action.payload.id)
      if (itemQtyToBeDecr[0].qty === 1) {
        return state
      }
      return {
        items: state.items.map(b => {
          if (action.payload.id === b.id) {
            return Object.assign({}, b, {
              qty: b.qty - 1
            })
          } else {
            return b
          }
        }),
        totalPrice: state.totalPrice - itemQtyToBeDecr[0].price
      }

    case CLEAR_CART:
      return {
        items: [],
        totalPrice: 0
      }
    default:
      return state
  }
}

const payment = (state = {
  items: [],
  emiList: [],
  ccNum: '',
  mon: '',
  year: '',
  cvv: '',
  cardType : ''
}, action) => {
  switch (action.type) {
    case 'FETCH_BANK_FUlFILLED':
      //const data  = action.payload.data
      //TODO: update with API on future
      const data = bankData; 
      return {
        ...state,
        items:data
      }
    break;
    case 'FETCH_EMI_FUlFILLED':
      return {
        ...state,
        emiList:emiData
      }
      break;
    case 'BANK_SELECTED':
        const bankId = action.payload
        const newData = {
          items: state.items.map(i => {
            i.isSelected = false;
            if (i.id === bankId) {
              i.isSelected = true;
            }
            return i
          })
        }
        return {...state, ...newData};
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
      return {...state, ...newEmiData};
    break;
    case 'SELECT_PAYMENT_OPTIONS':
      switch (action.payload.option) {
        case 'mon':
           return {...state, mon:action.payload.value}
        case 'year':
          return {...state, year:action.payload.value}
        case 'ccNum':
          return {...state, ccNum:action.payload.value}
        case 'cvv':
          return {...state, cvv:action.payload.value}
      }
    default:
      return state
  }
}

const rootReducer = combineReducers({
  products,
  etalase,
  cart,
  payment
})

export default rootReducer