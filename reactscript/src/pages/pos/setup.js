import React, { Component } from 'react'
import { View, Text } from 'react-native'
import Setup_pos from './setup_pos'
import Setup_payment from './setup_payment'
import Setup_cart from './setup_cart'
// import { Provider } from 'react-redux'
// import store from './store/Store'
// import PaymentBank from './components/PaymentBank'
// import Payment from './components/Payment'
// import PaymentProcessing from './components/PaymentProcessing'
// import PaymentInvoice from './components/PaymentInvoice'
// import TransactionHistory from './components/TransactionHistory'
// import PasswordPopup from './components/PasswordPopup'
// import POS from './components/POS'
// import CartContainer from './containers/CartContainer'
// import { StackNavigator } from 'react-navigation';

// // TODO: FOR UI testing purpose
// const App = StackNavigator({
//   POS: {screen: POS},
//   BankSelection : { screen: PaymentBank },
//   Payment: { screen: Payment },
//   PaymentProcessing: {screen: PaymentProcessing},
//   PaymentInvoice: {screen: PaymentInvoice},
//   TransactionHistory: {screen: TransactionHistory},
//   PasswordPopup: {screen: PasswordPopup}
// }, {headerMode: 'none'});


class Root extends Component {
  componentWillMount() {
    console.log(this.props)
  }

  render() {
    if (this.props.data.POS_PAGE === 'POS'){
      return <Setup_pos />
    } else if (this.props.data.POS_PAGE === 'PAYMENT'){
      return <Setup_payment />
    } else if (this.props.data.POS_PAGE === 'LOCAL_CART'){
      return <Setup_cart />
    }
  }
}
export default Root