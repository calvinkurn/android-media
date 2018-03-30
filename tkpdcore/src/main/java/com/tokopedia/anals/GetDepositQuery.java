package com.tokopedia.anals;

import com.apollographql.apollo.api.Field;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.ResponseFieldMapper;
import com.apollographql.apollo.api.ResponseReader;

import java.io.IOException;

import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Generated("Apollo GraphQL")
public final class GetDepositQuery implements Query<GetDepositQuery.Data, GetDepositQuery.Data, Operation.Variables> {
    public static final String OPERATION_DEFINITION = "query GetDepositQuery {\n"
            + "  saldo {\n"
            + "    __typename\n"
            + "    deposit_fmt\n"
            + "    deposit\n"
            + "  }\n"
            + "}";

    public static final String QUERY_DOCUMENT = OPERATION_DEFINITION;

    private final Variables variables;

    public GetDepositQuery() {
        this.variables = Operation.EMPTY_VARIABLES;
    }

    @Override
    public String queryDocument() {
        return QUERY_DOCUMENT;
    }

    @Override
    public Data wrapData(Data data) {
        return data;
    }

    @Override
    public Variables variables() {
        return variables;
    }

    @Override
    public ResponseFieldMapper<Data> responseFieldMapper() {
        return new Data.Mapper();
    }

    public static class Data implements Operation.Data {
        private final @Nullable
        Saldo saldo;

        public Data(@Nullable Saldo saldo) {
            this.saldo = saldo;
        }

        public @Nullable
        Saldo saldo() {
            return this.saldo;
        }

        @Override
        public String toString() {
            return "Data{"
                    + "saldo=" + saldo
                    + "}";
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof Data) {
                Data that = (Data) o;
                return ((this.saldo == null) ? (that.saldo == null) : this.saldo.equals(that.saldo));
            }
            return false;
        }

        @Override
        public int hashCode() {
            int h = 1;
            h *= 1000003;
            h ^= (saldo == null) ? 0 : saldo.hashCode();
            return h;
        }

        public static final class Mapper implements ResponseFieldMapper<Data> {
            final Saldo.Mapper saldoFieldMapper = new Saldo.Mapper();

            final Field[] fields = {
                    Field.forObject("saldo", "saldo", null, true, new Field.ObjectReader<Saldo>() {
                        @Override
                        public Saldo read(final ResponseReader reader) throws IOException {
                            return saldoFieldMapper.map(reader);
                        }
                    })
            };

            @Override
            public Data map(ResponseReader reader) throws IOException {
                final Saldo saldo = reader.read(fields[0]);
                return new Data(saldo);
            }
        }

        public static class Saldo {
            private final @Nonnull
            String deposit_fmt;

            private final int deposit;

            public Saldo(@Nonnull String deposit_fmt, int deposit) {
                this.deposit_fmt = deposit_fmt;
                this.deposit = deposit;
            }

            public @Nonnull
            String deposit_fmt() {
                return this.deposit_fmt;
            }

            public int deposit() {
                return this.deposit;
            }

            @Override
            public String toString() {
                return "Saldo{"
                        + "deposit_fmt=" + deposit_fmt + ", "
                        + "deposit=" + deposit
                        + "}";
            }

            @Override
            public boolean equals(Object o) {
                if (o == this) {
                    return true;
                }
                if (o instanceof Saldo) {
                    Saldo that = (Saldo) o;
                    return ((this.deposit_fmt == null) ? (that.deposit_fmt == null) : this.deposit_fmt.equals(that.deposit_fmt))
                            && this.deposit == that.deposit;
                }
                return false;
            }

            @Override
            public int hashCode() {
                int h = 1;
                h *= 1000003;
                h ^= (deposit_fmt == null) ? 0 : deposit_fmt.hashCode();
                h *= 1000003;
                h ^= deposit;
                return h;
            }

            public static final class Mapper implements ResponseFieldMapper<Saldo> {
                final Field[] fields = {
                        Field.forString("deposit_fmt", "deposit_fmt", null, false),
                        Field.forInt("deposit", "deposit", null, false)
                };

                @Override
                public Saldo map(ResponseReader reader) throws IOException {
                    final String deposit_fmt = reader.read(fields[0]);
                    final int deposit = reader.read(fields[1]);
                    return new Saldo(deposit_fmt, deposit);
                }
            }
        }
    }
}
