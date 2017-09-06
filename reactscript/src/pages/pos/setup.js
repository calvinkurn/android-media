import React, { Component } from 'react'
import { Provider } from 'react-redux'
import store from './store/Store'
import PaymentBank from './components/PaymentBank'
import Payment from './components/Payment'
import PaymentProcessing from './components/PaymentProcessing'
import POS from './components/POS'
import { StackNavigator } from 'react-navigation';

// TODO: FOR UI testing purpose
const App = StackNavigator({
  BankSelection : { screen: PaymentBank },
  Payment: { screen: Payment },
  PaymentProcessing: {screen: PaymentProcessing}
});


class Root extends Component {
  render() {
    return (
      <Provider store={store}>
        {this.props.data.POS_PAGE === 'POS' && <POS />}
        {this.props.data.POS_PAGE === 'PAYMENT' && <APP />}
      </Provider>
    )
  }
}
export default Root